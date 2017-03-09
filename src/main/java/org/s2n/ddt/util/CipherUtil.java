package org.s2n.ddt.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

public class CipherUtil {

	private final static Logger logger = LogManager.getLogger(CipherUtil.class);

	/*
	 * private static byte[] key = { 0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41,
	 * 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79 };//
	 * "thisIsASecretKey";
	 */

	private SecretKeySpec secretKey;
	private Cipher cipher;

	/**
	 * 
	 * param key1 shouldbe string of len 32
	 * @throws Exception 
	 * 
	 * 
	 * */
	public CipherUtil(final String key1) throws Exception  {
		try {

			if (key1 == null) {
				throw new IllegalArgumentException("Key cannaot be null");
			}
			double d = key1.length();
			logger.info(d + "  " + (d % 16f) + "\n");
			if (d % 16f != 0) {
				throw new IllegalArgumentException("Key length should be multiple of 16");
			}
			cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			byte[] key = key1.getBytes();
			secretKey = new SecretKeySpec(key, "AES");
		} catch (Exception e) {
			logger.error("Error while sending keys :" + e + "] key : " + key1, e);
			throw e;
		}
	}

	public synchronized String encrypt(String strToEncrypt) throws Exception {
		try {

			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			final byte[] b = cipher.doFinal(strToEncrypt.getBytes());
			/*
			 * final String encryptedString =
			 * Base64.encodeBase64String(cipher.doFinal
			 * (strToEncrypt.getBytes()));
			 */
			final String encryptedString;
			// encryptedString = Base64.encodeBase64(b).toString();
			encryptedString = Base64.encodeBytes(b);
			return encryptedString;
		} catch (Exception e) {
			logger.error("Error while encrypting :" + e + "] strToEncrypt :" + strToEncrypt, e);
			throw e;
		}
	}

	public synchronized String decrypt(String strToDecrypt) throws Exception {
		try {

			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			final String decryptedString = new String(cipher.doFinal(Base64.decode(strToDecrypt)));
			return decryptedString;
		} catch (Exception e) {
			logger.error("Error while decrypting :" + e + "] strToDecrypt : " + strToDecrypt, e);
			throw e;
		}
	}

	/*
	 * @Before public void setCipher() throws IOException{ java.io.File f = new
	 * java.io.File("./").getCanonicalFile(); System.out.println("f : " + f);
	 * String k = "0235tushar,fk45l" + "0235tushar,fk45l"; aaCipherUtils1(k); }
	 */

	// move to test junit

	public void cipher() {
		try {
			final String encryptedStr = encrypt("Apr@2013");
			logger.info("Encrypted : " + encryptedStr);
			final String decryptedStr = decrypt(encryptedStr);
			logger.info("Decrypted : " + decryptedStr);
		} catch (Exception e) {
			logger.error("Error in Clipher main   :" + e + "]");

		}
	}

	/**
	 * CommandLineParser parser = new PosixParser(); Options options = new
	 * Options(); Option help = new Option("help", "Display help"); Option
	 * encrypt = new Option("encrypt", true, " - string to encrypt"); Option
	 * decrypt = new Option("decrypt", true, " - string to decrypt");
	 * options.addOption(help); options.addOption(encrypt);
	 * options.addOption(decrypt); try { CommandLine cmd = parser.parse(options,
	 * args); if (cmd.hasOption("encrypt")) { final String strToEncrypt =
	 * cmd.getOptionValue("encrypt"); final String encryptedStr =
	 * CipherUtils.encrypt("abcd"); System.out.println("String to Encrypt : " +
	 * strToEncrypt); System.out.println("Encrypted : " + encryptedStr); } else
	 * if (cmd.hasOption("decrypt")) { final String strToDecrypt =
	 * cmd.getOptionValue("decrypt"); final String decryptedStr =
	 * CipherUtils.decrypt(strToDecrypt .trim());
	 * System.out.println("String To Decrypt : " + strToDecrypt);
	 * System.out.println("Decrypted : " + decryptedStr); } else { HelpFormatter
	 * formatter = new HelpFormatter(); formatter.printHelp("[-h] [-encrypt ]",
	 * options); } } catch (Exception e) {
	 * log.error("Error while parsing command ", e); }
	 */

	public static final String getVersion() {
		return "1";
	}
}
