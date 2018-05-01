//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.sun.javacard.clientlib.CardAccessor;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class SafeLibraryCardAccessor implements CardAccessor
{
   private static final byte BLOCK_SIZE                 =   16; 
   private static final byte CLA_SECURITY_BITS_MASK     =   (byte)0x0C;
   private static final byte CLA_ISO7816                =   (byte)0x00;
   private static final byte INS_SELECT                 =   (byte)0xA4;
   private static final byte OFFSET_CLA                 =   (byte)0;
   private static final byte OFFSET_INS                 =   (byte)1;
   private static final byte OFFSET_LC                  =   (byte)4;
   private static final byte OFFSET_CDATA               =   (byte)5;
   private static final byte OFFSET_SW1                 =   (byte)0; 
   private static final byte OFFSET_SW2                 =   (byte)1; 
   private static final byte OFFSET_RDATA               =   (byte)2;
   
   private final CardAccessor cardAccessor;
   private SecretKey key;
   private IvParameterSpec iv;
   private KeyPair asymKeyPair;
   private byte[] keyBytes; 
   private byte[] ivBytes;
   private Cipher cipher;
   
   public SafeLibraryCardAccessor(CardAccessor cardAccessor)
   {  
       this.cardAccessor = cardAccessor;
       initAsymKeyPair();
       initAES();
   }
   
   private void initAsymKeyPair()
   {
       try
       {
            KeyPairGenerator keyGen  =   KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            asymKeyPair              =   keyGen.generateKeyPair();
       }
       
       catch(Exception e)
       {
           System.out.println("[Init Key Pair Fail] " + e.getMessage());
       }
   }
   
   private byte[] signMessage(byte[] out)
   {
       try
       {
            Signature signer =   Signature.getInstance("SHA1WithRSA");
            signer.initSign(asymKeyPair.getPrivate());
            byte[] signature =   new byte[128];
            signer.update(out);
            signer.sign(signature, 0, 128);
            return signature;
       }
       
       catch(Exception e)
       {
           System.out.println("[Sign message fail] " + e.getMessage());
           return null;
       }
   }
   
   private boolean verifySignedMessage(byte[] signature, byte[] in)
   {
       try
       {
            Signature signer =  Signature.getInstance("SHA1WithRSA");
            signer.update(in);
            signer.initVerify(asymKeyPair.getPublic());

            return signer.verify(signature);
       }
       
       catch(Exception e)
       {
           System.out.println("[Verify message fail] " + e.getMessage());
           return false;
       }
   }
   
    private void initAES()
    {
       try
       {
            ivBytes          =      Config.DEFAULT_IV;
            keyBytes         =      Config.DEFAULT_KEY;
            iv               =      new IvParameterSpec(ivBytes);
            key              =      new SecretKeySpec(keyBytes, "AES");
            cipher           =      Cipher.getInstance("AES/CBC/NoPadding");
       }
       
       catch(NoSuchAlgorithmException | NoSuchPaddingException e)
       {
           System.out.println("[Init AES FAIL] " + e.getMessage());
       }
    }
   
   public byte[] exchangeAPDU(byte[] sendData) throws IOException
   {
      byte cla  =   (byte)(sendData[OFFSET_CLA] & (byte)0xFC);
      byte ins  =   sendData[OFFSET_INS];
      byte lc   =   sendData[OFFSET_LC];
      
      if (cla==CLA_ISO7816 && ins==INS_SELECT)
         return cardAccessor.exchangeAPDU(sendData);
      
      else
      {
        
          try
          {
            byte[] plaintext    =   pad(sendData, OFFSET_CDATA, lc);
            byte[] signature    =   signMessage(plaintext);
            
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            byte[] ciphertext;
            ciphertext = cipher.doFinal(plaintext);

            if (ciphertext.length > 255)
               throw new IOException("Command APDU too long");

            byte[] encryptedCommand = new byte[OFFSET_CDATA+ciphertext.length + 1];
            System.arraycopy(sendData, 0, encryptedCommand, 0, OFFSET_CDATA-1);
            encryptedCommand[OFFSET_CLA] |= CLA_SECURITY_BITS_MASK;
            encryptedCommand[OFFSET_LC] = (byte)ciphertext.length;

            System.arraycopy(ciphertext, 0, encryptedCommand, OFFSET_CDATA, ciphertext.length);
            encryptedCommand[encryptedCommand.length -1 ] = sendData[sendData.length -1];

            byte[] encryptedResponse = cardAccessor.exchangeAPDU(encryptedCommand);

            if ((encryptedResponse.length-2) % BLOCK_SIZE != 0)
               throw new IOException("Illegal block size in response");

            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] deciphertext;

            deciphertext                    =   cipher.doFinal(encryptedResponse, OFFSET_RDATA, encryptedResponse.length-2);
            byte numPadding                 =   deciphertext[deciphertext.length-1];
            int unpaddedLength              =   deciphertext.length - numPadding;
            byte[] decryptedResponse        =   new byte[OFFSET_RDATA+unpaddedLength];
            decryptedResponse[OFFSET_SW1]   =   encryptedResponse[OFFSET_SW1];
            decryptedResponse[OFFSET_SW2]   =   encryptedResponse[OFFSET_SW2];

            System.arraycopy(deciphertext, 0, decryptedResponse, OFFSET_RDATA, unpaddedLength);
            boolean verified                =   verifySignedMessage(signature, decryptedResponse);
            
            if(verified) return decryptedResponse;
            else throw new IOException("Received non-authenticated response");
          }
          
          catch(InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | IOException e)
          {
              System.out.println("[Exchange APDU Fail] " + e.getMessage());
              return null;
          }
      }
   } 
   
   public void closeCard() throws Exception
   {  
       cardAccessor.closeCard();
   }
   
   private byte[] pad(byte[] unpadded, int start, int unpaddedLength)
   {  
        int numBlocks       =   (unpaddedLength+BLOCK_SIZE) / BLOCK_SIZE;
        int paddedLength    =   numBlocks*BLOCK_SIZE;
        byte[] padded       =   new byte[paddedLength];
        System.arraycopy(unpadded, start, padded, 0, unpaddedLength);
        byte numPadding     =   (byte)(paddedLength - unpaddedLength);
        
        for (int i=unpaddedLength; i<paddedLength; i++)
           padded[i] = numPadding;
        
        return padded;
   }
}
