package com.jordi.tugasakhir_skripsi_mj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Deciphering {
    public void decipher(InputStream infile, byte[] inkey, File myExFile) throws IOException,
            NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException, InvalidKeyException, ShortBufferException,
            BadPaddingException, IllegalBlockSizeException {

        //Input file video.
        ConvertFiletoBytes cfb = new ConvertFiletoBytes();
        byte[] bytesVideo = cfb.readBytes(infile);

        //Input Kunci.
        byte[] kunci = inkey;

        //Byte IV.
        byte[] byteIV = new byte[]{
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };

        //Cipher Configuration.
        SecretKeySpec key = new SecretKeySpec(kunci, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(byteIV);
        Cipher cipher = Cipher.getInstance("AES", "BC");

        //Deciphering.
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] plainVideo = new byte[cipher.getOutputSize(bytesVideo.length)];
        int pjgPlainVideo = cipher.update(bytesVideo, 0,
                bytesVideo.length, plainVideo, 0);
        cipher.doFinal(plainVideo, pjgPlainVideo);

        //Menulis file video baru hasil dekripsi.
        FileOutputStream fos = new FileOutputStream(myExFile);
        for (int x = 0; x < plainVideo.length; x++) {
            fos.write(plainVideo[x]);
        }
        fos.close();
    }
}
