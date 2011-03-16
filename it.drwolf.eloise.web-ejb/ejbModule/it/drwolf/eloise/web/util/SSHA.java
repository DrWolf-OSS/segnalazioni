package it.drwolf.eloise.web.util;

/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.php.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at
 * http://www.opensource.org/licenses/cddl1.php.  If applicable,
 * add the following below this CDDL HEADER, with the fields enclosed
 * by brackets "[]" replaced with your own identifying * information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Portions Copyright 2006 Sun Microsystems, Inc.
 */
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Arrays;

/**
 * This class provides a mechanism for dealing with salted SHA-1 passwords as
 * used by the Sun Java System Directory Server. It is intended primarily for
 * illustration purposes, and should not be used to pre-encode values when
 * adding them to the Directory Server, nor should it be used for externally
 * verifying credentials. Both of these can bypass password policy restrictions
 * that might otherwise be in place.
 */
public class SSHA {
	/**
	 * The set of characters that may be used in base64-encoded values.
	 */
	public static final char[] BASE64_ALPHABET = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
			+ "0123456789+/").toCharArray();

	/**
	 * Decodes the provided set of base64-encoded data.
	 * 
	 * @param encodedData
	 *            The base64-encoded data to decode.
	 * 
	 * @return The decoded raw data.
	 * 
	 * @throws ParseException
	 *             If a problem occurs while attempting to decode the provided
	 *             data.
	 */
	public static byte[] base64Decode(String encodedData) throws ParseException {
		// The encoded value must have length that is a multiple of four bytes.
		int length = encodedData.length();
		if ((length % 4) != 0) {
			throw new ParseException(
					"The encoded value had a length that was not "
							+ "a multiple of four bytes", 0);
		}

		ByteBuffer buffer = ByteBuffer.allocate(length);
		for (int i = 0; i < length; i += 4) {
			boolean append = true;
			int value = 0;

			for (int j = 0; j < 4; j++) {
				switch (encodedData.charAt(i + j)) {
				case 'A':
					value <<= 6;
					break;
				case 'B':
					value = (value << 6) | 0x01;
					break;
				case 'C':
					value = (value << 6) | 0x02;
					break;
				case 'D':
					value = (value << 6) | 0x03;
					break;
				case 'E':
					value = (value << 6) | 0x04;
					break;
				case 'F':
					value = (value << 6) | 0x05;
					break;
				case 'G':
					value = (value << 6) | 0x06;
					break;
				case 'H':
					value = (value << 6) | 0x07;
					break;
				case 'I':
					value = (value << 6) | 0x08;
					break;
				case 'J':
					value = (value << 6) | 0x09;
					break;
				case 'K':
					value = (value << 6) | 0x0A;
					break;
				case 'L':
					value = (value << 6) | 0x0B;
					break;
				case 'M':
					value = (value << 6) | 0x0C;
					break;
				case 'N':
					value = (value << 6) | 0x0D;
					break;
				case 'O':
					value = (value << 6) | 0x0E;
					break;
				case 'P':
					value = (value << 6) | 0x0F;
					break;
				case 'Q':
					value = (value << 6) | 0x10;
					break;
				case 'R':
					value = (value << 6) | 0x11;
					break;
				case 'S':
					value = (value << 6) | 0x12;
					break;
				case 'T':
					value = (value << 6) | 0x13;
					break;
				case 'U':
					value = (value << 6) | 0x14;
					break;
				case 'V':
					value = (value << 6) | 0x15;
					break;
				case 'W':
					value = (value << 6) | 0x16;
					break;
				case 'X':
					value = (value << 6) | 0x17;
					break;
				case 'Y':
					value = (value << 6) | 0x18;
					break;
				case 'Z':
					value = (value << 6) | 0x19;
					break;
				case 'a':
					value = (value << 6) | 0x1A;
					break;
				case 'b':
					value = (value << 6) | 0x1B;
					break;
				case 'c':
					value = (value << 6) | 0x1C;
					break;
				case 'd':
					value = (value << 6) | 0x1D;
					break;
				case 'e':
					value = (value << 6) | 0x1E;
					break;
				case 'f':
					value = (value << 6) | 0x1F;
					break;
				case 'g':
					value = (value << 6) | 0x20;
					break;
				case 'h':
					value = (value << 6) | 0x21;
					break;
				case 'i':
					value = (value << 6) | 0x22;
					break;
				case 'j':
					value = (value << 6) | 0x23;
					break;
				case 'k':
					value = (value << 6) | 0x24;
					break;
				case 'l':
					value = (value << 6) | 0x25;
					break;
				case 'm':
					value = (value << 6) | 0x26;
					break;
				case 'n':
					value = (value << 6) | 0x27;
					break;
				case 'o':
					value = (value << 6) | 0x28;
					break;
				case 'p':
					value = (value << 6) | 0x29;
					break;
				case 'q':
					value = (value << 6) | 0x2A;
					break;
				case 'r':
					value = (value << 6) | 0x2B;
					break;
				case 's':
					value = (value << 6) | 0x2C;
					break;
				case 't':
					value = (value << 6) | 0x2D;
					break;
				case 'u':
					value = (value << 6) | 0x2E;
					break;
				case 'v':
					value = (value << 6) | 0x2F;
					break;
				case 'w':
					value = (value << 6) | 0x30;
					break;
				case 'x':
					value = (value << 6) | 0x31;
					break;
				case 'y':
					value = (value << 6) | 0x32;
					break;
				case 'z':
					value = (value << 6) | 0x33;
					break;
				case '0':
					value = (value << 6) | 0x34;
					break;
				case '1':
					value = (value << 6) | 0x35;
					break;
				case '2':
					value = (value << 6) | 0x36;
					break;
				case '3':
					value = (value << 6) | 0x37;
					break;
				case '4':
					value = (value << 6) | 0x38;
					break;
				case '5':
					value = (value << 6) | 0x39;
					break;
				case '6':
					value = (value << 6) | 0x3A;
					break;
				case '7':
					value = (value << 6) | 0x3B;
					break;
				case '8':
					value = (value << 6) | 0x3C;
					break;
				case '9':
					value = (value << 6) | 0x3D;
					break;
				case '+':
					value = (value << 6) | 0x3E;
					break;
				case '/':
					value = (value << 6) | 0x3F;
					break;
				case '=':
					append = false;
					switch (j) {
					case 2:
						buffer.put((byte) ((value >>> 4) & 0xFF));
						break;
					case 3:
						buffer.put((byte) ((value >>> 10) & 0xFF));
						buffer.put((byte) ((value >>> 2) & 0xFF));
						break;
					}
					break;
				default:
					throw new ParseException("Invalid base64 character "
							+ encodedData.charAt(i + j), i + j);
				}

				if (!append) {
					break;
				}
			}

			if (append) {
				buffer.put((byte) ((value >>> 16) & 0xFF));
				buffer.put((byte) ((value >>> 8) & 0xFF));
				buffer.put((byte) (value & 0xFF));
			} else {
				break;
			}
		}

		buffer.flip();
		byte[] returnArray = new byte[buffer.limit()];
		buffer.get(returnArray);
		return returnArray;
	}

	/**
	 * Base64-encodes the provided raw data.
	 * 
	 * @param rawData
	 *            The raw data to encode.
	 * 
	 * @return The base64-encoded representation of the provided raw data.
	 */
	public static String base64Encode(byte[] rawData) {
		StringBuffer buffer = new StringBuffer(4 * rawData.length / 3);

		int pos = 0;
		int iterations = rawData.length / 3;
		for (int i = 0; i < iterations; i++) {
			int value = ((rawData[pos++] & 0xFF) << 16)
					| ((rawData[pos++] & 0xFF) << 8) | (rawData[pos++] & 0xFF);

			buffer.append(SSHA.BASE64_ALPHABET[(value >>> 18) & 0x3F]);
			buffer.append(SSHA.BASE64_ALPHABET[(value >>> 12) & 0x3F]);
			buffer.append(SSHA.BASE64_ALPHABET[(value >>> 6) & 0x3F]);
			buffer.append(SSHA.BASE64_ALPHABET[value & 0x3F]);
		}

		switch (rawData.length % 3) {
		case 1:
			buffer.append(SSHA.BASE64_ALPHABET[(rawData[pos] >>> 2) & 0x3F]);
			buffer.append(SSHA.BASE64_ALPHABET[(rawData[pos] << 4) & 0x3F]);
			buffer.append("==");
			break;
		case 2:
			int value = ((rawData[pos++] & 0xFF) << 8) | (rawData[pos] & 0xFF);
			buffer.append(SSHA.BASE64_ALPHABET[(value >>> 10) & 0x3F]);
			buffer.append(SSHA.BASE64_ALPHABET[(value >>> 4) & 0x3F]);
			buffer.append(SSHA.BASE64_ALPHABET[(value << 2) & 0x3F]);
			buffer.append("=");
			break;
		}

		return buffer.toString();
	}

	/**
	 * Determines whether the provided clear-text password is a match for the
	 * given SSHA-encoded password.
	 * 
	 * @param clearPasswordBytes
	 *            The bytes that make up the clear-text password.
	 * @param sshaPasswordString
	 *            The SSHA-encoded password string to compare against the
	 *            clear-text password.
	 * 
	 * @return <CODE>true</CODE> if the provided clear-text password matches
	 *         the SSHA-encoded password, or <CODE>false</CODE> if not.
	 * 
	 * @throws NoSuchAlgorithmException
	 *             If the JVM doesn't know how to generate SHA-1 digests for
	 *             some reason.
	 * 
	 * @throws ParseException
	 *             If a problem occurs while base64-decoding the password data.
	 */
	public static boolean comparePassword(byte[] clearPasswordBytes,
			String sshaPasswordString) throws NoSuchAlgorithmException,
			ParseException {
		// Base64-decode the value of the encoded password string and break it
		// up
		// into the SHA-1 digest and salt portions.
		byte[] digestPlusSalt = SSHA.base64Decode(sshaPasswordString
				.substring(6));
		byte[] saltBytes = new byte[8];
		byte[] digestBytes = new byte[digestPlusSalt.length - 8];

		System.arraycopy(digestPlusSalt, 0, digestBytes, 0, digestBytes.length);
		System.arraycopy(digestPlusSalt, digestBytes.length, saltBytes, 0,
				saltBytes.length);

		// Create a byte array that appends the salt bytes to the clear-text
		// password bytes.
		byte[] passwordPlusSalt = new byte[clearPasswordBytes.length
				+ saltBytes.length];
		System.arraycopy(clearPasswordBytes, 0, passwordPlusSalt, 0,
				clearPasswordBytes.length);
		System.arraycopy(saltBytes, 0, passwordPlusSalt,
				clearPasswordBytes.length, saltBytes.length);

		// Get a message digest that can generate SHA-1 hashes and use it to
		// digest
		// the password plus the salt.
		MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
		byte[] passwordPlusSaltHash = sha1Digest.digest(passwordPlusSalt);

		// If the provided password is correct, then the contents of the
		// digestBytes
		// array will match the contents of the passwordPlusSaltHash array.
		return Arrays.equals(digestBytes, passwordPlusSaltHash);
	}

	/**
	 * Encodes the provided password using the SSHA password storage scheme.
	 * 
	 * @param clearPasswordBytes
	 *            The bytes that make up the clear-text password to encode.
	 * 
	 * @return The SSHA-encoded password string.
	 * 
	 * @throws NoSuchAlgorithmException
	 *             If the JVM doesn't know how to generate SHA-1 digests for
	 *             some reason.
	 */
	public static String encodePassword(byte[] clearPasswordBytes)
			throws NoSuchAlgorithmException {
		// Generate the 64-bit salt to use with the password.
		byte[] saltBytes = new byte[8];
		new SecureRandom().nextBytes(saltBytes);

		// Create a byte array that appends the salt bytes to the clear-text
		// password bytes.
		byte[] passwordPlusSalt = new byte[clearPasswordBytes.length
				+ saltBytes.length];
		System.arraycopy(clearPasswordBytes, 0, passwordPlusSalt, 0,
				clearPasswordBytes.length);
		System.arraycopy(saltBytes, 0, passwordPlusSalt,
				clearPasswordBytes.length, saltBytes.length);

		// Get a message digest that can generate SHA-1 hashes and use it to
		// digest
		// the password plus the salt.
		MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
		byte[] passwordPlusSaltHash = sha1Digest.digest(passwordPlusSalt);

		// Create a byte array that appends the salt bytes to the SHA-1 digest.
		byte[] digestPlusSalt = new byte[passwordPlusSaltHash.length
				+ saltBytes.length];
		System.arraycopy(passwordPlusSaltHash, 0, digestPlusSalt, 0,
				passwordPlusSaltHash.length);
		System.arraycopy(saltBytes, 0, digestPlusSalt,
				passwordPlusSaltHash.length, saltBytes.length);

		// Base64-encode the resulting array and use it to create the formatted
		// string.
		return "{SSHA}" + SSHA.base64Encode(digestPlusSalt);
	}

	/**
	 * Either encodes a clear-text password using SSHA or compares a clear-text
	 * password against a SSHA-encoded password. To encode a clear-text
	 * password, use "java SSHA -c {clearPassword}". To compare a clear-text
	 * password against a SSHA-encoded password string, use "java SSHA -c
	 * {clearPassword} -e {encodedPassword}".
	 * 
	 * @param args
	 *            The command-line arguments provided by the client.
	 */
	public static void main(String[] args) throws Exception {
		String usageString = "USAGE:  java SSHA -c {clearPassword} "
				+ "[-e {encodedPassword}]";
		if ((args.length != 2) && (args.length != 4)) {
			System.err.println(usageString);
			System.exit(1);
		}

		byte[] clearPassword = null;
		String encodedPassword = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-c")) {
				clearPassword = args[++i].getBytes("UTF-8");
			} else if (args[i].equals("-e")) {
				encodedPassword = args[++i];
			} else {
				System.err
						.println("ERROR:  Unrecognized argument:  " + args[i]);
				System.err.println(usageString);
				System.exit(1);
			}
		}

		if (clearPassword == null) {
			System.err
					.println("ERROR:  No clear-text password provided (use -c)");
			System.err.println(usageString);
			System.exit(1);
		} else if (encodedPassword == null) {
			System.out.println(SSHA.encodePassword(clearPassword));
		} else {
			if (SSHA.comparePassword(clearPassword, encodedPassword)) {
				System.err
						.println("The clear-text password matches the encoded "
								+ "password");
			} else {
				System.err
						.println("The clear-text password does not match the "
								+ "encoded password");
			}
		}
	}
}
