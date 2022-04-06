package com.example.springboot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
import java.util.Base64;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.SignRequest;
import com.amazonaws.services.kms.model.SignResult;

@RestController
public class HelloController {

	@PostMapping("/sign")
	public String sign(@RequestBody String signData) throws InvalidKeyException, InvalidKeySpecException,
			NoSuchAlgorithmException, SignatureException, IOException {
		byte[] signedData = null;
		String encoded = null;
		try {
			signedData = this.getSignedData(signData);
			encoded = Base64.getEncoder().encodeToString(signedData);
			verifySgn(encoded, signData);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encoded;
	}

	@PostMapping("/verify")
	public boolean verify(@RequestBody VerifyData verifyData) throws InvalidKeyException, InvalidKeySpecException,
			NoSuchAlgorithmException, SignatureException, IOException {
		boolean signDataVerificationStatus = false;
		try {
			signDataVerificationStatus = this.verifySgn(verifyData.getSignedData(), verifyData.getOriginalData());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return signDataVerificationStatus;
	}

	private byte[] getSignedData(String signData) throws InvalidKeyException, InvalidKeySpecException,
			NoSuchAlgorithmException, SignatureException, IOException {

		SignRequest signRequest = new SignRequest();

		String kmsKey = "alias/demo_test_alias";
		signRequest.setKeyId(kmsKey);
		ByteBuffer buf = ByteBuffer.wrap(signData.getBytes());
		signRequest.setMessage(buf);
		signRequest.setSigningAlgorithm("RSASSA_PKCS1_V1_5_SHA_256");
		AWSKMS client = AWSKMSClientBuilder.defaultClient();
		SignResult signResult = client.sign(signRequest);
		byte[] sign = signResult.getSignature().array();
		System.out.println("Signed Data ->> " + sign);
		System.out.println("with key " + kmsKey);

		this.verifySgn(sign, signData);
		return sign;
		/*********************************
		 * verify with public key
		 **************************************************/

	}

	boolean verifySgn(byte[] signatureBytes, String message) throws InvalidKeySpecException, NoSuchAlgorithmException,
			SignatureException, IOException, InvalidKeyException {
		String base64EncodedKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyecvGet46RPOs0gVI+hG\n"
				+ "5/NGm6uoEtgytEBwlnG2MNYU542JWrO1BpjYQM54eqHLWs3JVw+j4y2TVQgdugj6\n"
				+ "2sdLSDh8LxTf2PcO+z3qhYLmenfIw0Pu5o1Pnj+R1qew5aXqdtS7r7bJlu7zSK/h\n"
				+ "h7OkPWJnKsWcWgoOzta/yfk3GH4B15BCjplK0W3C8ji98C10BJi0suAoh+ZTR6dU\n"
				+ "CIS/CG0u/bseF4P+3ITOAsg1GGPFHD3Fcd24408XMZpm4x835Mi42XU5uyIzqNCv\n"
				+ "LG7QNfgilVgMJW8ZiP+MzEJ+UBQEcypreOElxbERLVMqmuYw/r8BsU8Fu5+FYyv6\n" + "IwIDAQAB";

		byte[] pubKeyBytes = Base64.getMimeDecoder().decode(base64EncodedKey);

		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);

		PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);

		Signature sig = Signature.getInstance("SHA256withRSA");

		sig.initVerify(publicKey);

		sig.update(message.getBytes());
		boolean signatureValid = sig.verify(signatureBytes);
		System.out.println("***overridded message with alias **********" + signatureValid);
		return signatureValid;
	}

	boolean verifySgn(String signatureBytes, String message) throws InvalidKeySpecException, NoSuchAlgorithmException,
			SignatureException, IOException, InvalidKeyException {
		System.out.println("STRINGGGGGGGGGGGGGGGGGGGGGGG");
		byte[] byetarray = Base64.getDecoder().decode(signatureBytes);
		String base64EncodedKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyecvGet46RPOs0gVI+hG\n"
				+ "5/NGm6uoEtgytEBwlnG2MNYU542JWrO1BpjYQM54eqHLWs3JVw+j4y2TVQgdugj6\n"
				+ "2sdLSDh8LxTf2PcO+z3qhYLmenfIw0Pu5o1Pnj+R1qew5aXqdtS7r7bJlu7zSK/h\n"
				+ "h7OkPWJnKsWcWgoOzta/yfk3GH4B15BCjplK0W3C8ji98C10BJi0suAoh+ZTR6dU\n"
				+ "CIS/CG0u/bseF4P+3ITOAsg1GGPFHD3Fcd24408XMZpm4x835Mi42XU5uyIzqNCv\n"
				+ "LG7QNfgilVgMJW8ZiP+MzEJ+UBQEcypreOElxbERLVMqmuYw/r8BsU8Fu5+FYyv6\n"
				+ "IwIDAQAB";

		byte[] pubKeyBytes = Base64.getMimeDecoder().decode(base64EncodedKey);

		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);

		PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);

		Signature sig = Signature.getInstance("SHA256withRSA");

		sig.initVerify(publicKey);

		sig.update(message.getBytes());
		boolean signatureValid = sig.verify(byetarray);
		System.out.println("***overridded message with alias **********" + signatureValid);
		return signatureValid;
	}

	private byte[] getSignatureBytes(byte[] signatureBytes) throws IOException {

		Resource resource = new ClassPathResource("signsid.txt");
		InputStream inputStream = resource.getInputStream();

		byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
		String s = "ohFjHK3o/RiI5QNCtpsL8LUwxk6Aw9ni66qtxGfuJOckakwvQO2Cva5w9DQmkdo6H6ITMm7GqUYxhPtNgWrQSU1Frt9bDcPZVdxUg0clnbbI7cUMG+C2bsnUYn/3gdpwxcJbwnceFArLROhreb3XGb69NJV5shBw57TaoA1v9zjdmjvTG463i3ClQTRPJ4nKeqJkyifgcAx5otiWOSdrJFnDsIzGTIFN3GnvJSXwz5zs8ii9StCca8XlI0g02cRMaJDiJ6Ctzo2WlorttM8IfmyC8eqCnBc5FCOo1gxw+2gfCxC0jHHFcw3DFEmN+M8PhH2YhqKSkYZZKbA5//WgbQ==";
		return Base64.getDecoder().decode(s);
		// return bdata;
	}

	byte[] getSignBytes() throws IOException {
		FileInputStream fis = new FileInputStream(new ClassPathResource("signsid.txt").getFile());
		InputStreamReader is = new InputStreamReader(fis, "UTF-8");

		BufferedReader in = new BufferedReader(is);

		String line;
		String signdatas = "";
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			signdatas += line.trim();
		}
		in.close();
		return signdatas.getBytes();
	}

	private byte[] getRawMessage() throws IOException {
		Resource resource = new ClassPathResource("message.txt");
		InputStream inputStream = resource.getInputStream();

		byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
		return bdata;
	}

	

	private Certificate loadCertificate(String filename) throws FileNotFoundException, CertificateException {
		FileInputStream fis = new FileInputStream(filename);
		BufferedInputStream bis = new BufferedInputStream(fis);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return cf.generateCertificate(bis);
	}

	public static void main(String[] args) throws InvalidKeyException, InvalidKeySpecException,
			NoSuchAlgorithmException, SignatureException, IOException {
		HelloController h = new HelloController();
		VerifyData verifyData = new VerifyData();
		verifyData.setOriginalData("dvdvdsvdsvdsvdvdv");
		verifyData.setSignedData("tpA6tEV2wxE0uzRXO/s60CfccLYYnNH2BNt4kxLsN8dsTeoaqs2TdFmiF/luu4Hg9P2ZML/6mS1np7NHIIST2xHhIHgad3KNfIoyx61kkoi9Y73ESy2FYA+pwtbm+CTrt0Y4qOXUe/h/MptjHrljYQ8KTRrPX1FcuPMNwVmrma86KiSurRhFf/amt9LbLrx1WOW/ki6IP2K2yRq29DRTwTKLmbSfkejxzfTkDHaK9Kwtc4YXEkIThjj31HAYiAUFL4R0cZhV+QuSXYU0afNtfnur0l20+7bYnLIZEUB8GZJA271jWEwP5mQVzEIjCQXxnxAezuceBllxiO/eojy11w==");
		h.verifySgn(verifyData.getSignedData(), verifyData.getOriginalData());
	}

}
