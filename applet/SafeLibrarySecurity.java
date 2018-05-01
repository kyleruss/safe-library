//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

package applet;

import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.JCSystem;
import javacard.framework.Util;
import javacard.framework.service.BasicService;
import javacard.framework.service.SecurityService;
import javacard.security.AESKey;
import javacard.security.KeyBuilder;
import javacard.security.CryptoException;
import javacardx.crypto.Cipher;

public class SafeLibrarySecurity extends BasicService implements SecurityService
{
   private static final byte BLOCK_SIZE             =   16; 
   private static final byte CLA_SECURITY_BITS_MASK =   (byte) 0x0C;
   private static final byte OFFSET_OUT_LA          =   (byte) 4;
   private static final byte OFFSET_OUT_RDATA       =   (byte) 5;
   
   private boolean appProviderAuthenticated, cardIssuerAuthenticated;
   private boolean cardHolderAuthenticated;
   private byte sessionProperties; 
   private byte commandProperties; 
   private AESKey key;
   private byte[] keyBytes;
   private byte[] ivBytes;
   private Cipher cipher; 
   
   public SafeLibrarySecurity()
   {
       super();
   
       resetSecuritySettings();
       initAES();
   }
   
   private void initAES()
   {
       ivBytes          =       new byte[] {66, 49, 70, 39, 120, -90, 81, -83, 60, -19, 6, 123, 53, 91, -80, -89};
       keyBytes         =       new byte[] {103, -125, -92, 79, -126, -49, 48, -84, -85, 113, -13, 41, -58, -106, -17, 31};
       key              =	(AESKey)KeyBuilder.buildKey(KeyBuilder.TYPE_AES_TRANSIENT_DESELECT, KeyBuilder.LENGTH_AES_128, false);
       cipher           =	Cipher.getInstance(Cipher.ALG_AES_BLOCK_128_CBC_NOPAD, false);
   }
   
    private void resetSecuritySettings()
    { 
        appProviderAuthenticated  =   false;
        cardIssuerAuthenticated   =   false;
        cardHolderAuthenticated   =   false;
        sessionProperties         =   0;
        commandProperties         =   0;
    }

   @Override
   public boolean processDataIn(APDU apdu)
   {  if (selectingApplet())
      {  
         resetSecuritySettings();
         return false; 
      }
   
      else if (!apdu.isSecureMessagingCLA())
      {  
         commandProperties &=
            ~(SecurityService.PROPERTY_INPUT_CONFIDENTIALITY |
            SecurityService.PROPERTY_OUTPUT_CONFIDENTIALITY);
         return false;
      }
      else
      { 
         commandProperties |=
            (SecurityService.PROPERTY_INPUT_CONFIDENTIALITY |
            SecurityService.PROPERTY_OUTPUT_CONFIDENTIALITY);
         
         byte[] buffer = apdu.getBuffer();
         byte lc = buffer[ISO7816.OFFSET_LC];
         byte le = buffer[(short)(ISO7816.OFFSET_LC+lc+1)];
         
         if (lc % BLOCK_SIZE != 0)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);   
         
         if (!key.isInitialized())
            key.setKey(keyBytes, (short)0);
         
         cipher.init(key, Cipher.MODE_DECRYPT, ivBytes, (short)0,(short)ivBytes.length);
         
         byte[] deciphertext = JCSystem.makeTransientByteArray(lc, JCSystem.CLEAR_ON_DESELECT);
         cipher.doFinal(buffer, ISO7816.OFFSET_CDATA, lc, deciphertext, (short)0);
         
         byte numPadding = deciphertext[(short)(lc-1)];
         byte unpaddedLength = (byte)(lc - numPadding);
         Util.arrayCopyNonAtomic(deciphertext, (short)0,buffer, ISO7816.OFFSET_CDATA, unpaddedLength);
         
         buffer[ISO7816.OFFSET_LC] = unpaddedLength;
         buffer[(short)(ISO7816.OFFSET_LC + unpaddedLength + 1)]    =   le;
         buffer[ISO7816.OFFSET_CLA] &= ~CLA_SECURITY_BITS_MASK;
         return true;
      }
   }
   
   @Override
   public boolean processDataOut(APDU apdu)
   {  
       if (selectingApplet()) return false; 
   
      else
      { 
         byte[] buffer = apdu.getBuffer();
         
         if (!key.isInitialized())
            key.setKey(keyBytes, (short)0);
         
         cipher.init(key, Cipher.MODE_ENCRYPT, ivBytes, (short)0,(short)ivBytes.length);
         byte unpaddedLength = (byte)(buffer[OFFSET_OUT_LA] & 0xFF);
         
         short numBlocks    =   (short)((short)(unpaddedLength+BLOCK_SIZE) / BLOCK_SIZE);
         short paddedLength =   (short)(numBlocks*BLOCK_SIZE);
         
         byte[] padded = JCSystem.makeTransientByteArray(paddedLength, JCSystem.CLEAR_ON_DESELECT);
         Util.arrayCopyNonAtomic(buffer, OFFSET_OUT_RDATA, padded,(short) 0, unpaddedLength);
         
         byte numPadding = (byte)(paddedLength - unpaddedLength);
         for (short i = unpaddedLength; i < paddedLength; i++)
            padded[i] = numPadding;
         
         if ((short)(OFFSET_OUT_RDATA-1+paddedLength) > buffer.length)
            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
         
         cipher.doFinal(padded, (short)0, (short)paddedLength, buffer, OFFSET_OUT_RDATA);
         buffer[OFFSET_OUT_LA] = (byte)paddedLength;
         return true; 
      }
   }

   @Override
   public boolean isAuthenticated(short principal)
   {  
      switch (principal)
      {  
          case PRINCIPAL_APP_PROVIDER : return appProviderAuthenticated;
          case PRINCIPAL_CARD_ISSUER : return cardIssuerAuthenticated;
          case PRINCIPAL_CARDHOLDER : return cardHolderAuthenticated;
          default : return false;
      }
   }
   
   @Override
   public boolean isChannelSecure(byte properties)
   { 
       return (sessionProperties & properties) != 0;
   }
   
   @Override
   public boolean isCommandSecure(byte properties)
   {  
       return (commandProperties & properties) != 0;
   }
}
