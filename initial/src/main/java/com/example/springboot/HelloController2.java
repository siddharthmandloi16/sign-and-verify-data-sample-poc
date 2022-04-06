/*
 * package com.example.springboot;
 * 
 * import java.io.BufferedInputStream; import java.io.BufferedReader; import
 * java.io.File; import java.io.FileInputStream; import
 * java.io.FileNotFoundException; import java.io.IOException; import
 * java.io.InputStream; import java.io.InputStreamReader; import
 * java.io.UnsupportedEncodingException; import java.net.URISyntaxException;
 * import java.nio.ByteBuffer; import java.nio.charset.Charset; import
 * java.nio.charset.StandardCharsets; import java.nio.file.Files; import
 * java.nio.file.Paths; import java.security.InvalidKeyException; import
 * java.security.KeyFactory; import java.security.NoSuchAlgorithmException;
 * import java.security.PrivateKey; import java.security.PublicKey; import
 * java.security.Signature; import java.security.SignatureException; import
 * java.security.cert.Certificate; import
 * java.security.cert.CertificateException; import
 * java.security.cert.CertificateFactory; import
 * java.security.interfaces.RSAPrivateKey; import
 * java.security.interfaces.RSAPublicKey; import
 * java.security.spec.InvalidKeySpecException; import
 * java.security.spec.PKCS8EncodedKeySpec; import
 * java.security.spec.X509EncodedKeySpec; //import java.util.Base64; import
 * java.util.Base64;
 * 
 * import org.springframework.core.io.ClassPathResource; import
 * org.springframework.core.io.Resource; import
 * org.springframework.util.FileCopyUtils; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.amazonaws.services.kms.AWSKMS; import
 * com.amazonaws.services.kms.AWSKMSClientBuilder; import
 * com.amazonaws.services.kms.model.SignRequest; import
 * com.amazonaws.services.kms.model.SignResult; import
 * com.amazonaws.services.kms.model.VerifyRequest;
 * 
 * @RestController public class HelloController2 {
 * 
 * @GetMapping("/") public String index() throws InvalidKeyException,
 * InvalidKeySpecException, NoSuchAlgorithmException, SignatureException,
 * IOException { try { this.getSignedData(); } catch
 * (UnsupportedEncodingException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } return "Greetings from Spring Boot!"; }
 * 
 * private void getSignedData() throws InvalidKeyException,
 * InvalidKeySpecException, NoSuchAlgorithmException, SignatureException,
 * IOException {
 * 
 * SignRequest signRequest = new SignRequest();
 * System.out.println("111111111111111111111111111111111111111"); String kmsKey
 * = "2d5c0b78-59aa-468e-91d5-b7e68dbbb42a"; signRequest.setKeyId(kmsKey);
 * String signData = "ThisDataNeedsToBeSignned"; ByteBuffer buf =
 * ByteBuffer.wrap(signData.getBytes()); signRequest.setMessage(buf);
 * System.out.println("22222222222222222222222222222222 " + buf);
 * signRequest.setSigningAlgorithm("RSASSA_PKCS1_V1_5_SHA_256"); AWSKMS client =
 * AWSKMSClientBuilder.defaultClient();
 * System.out.println("33333333333333333333333333333 " + client); SignResult
 * signResult = client.sign(signRequest);
 * System.out.println("44444444444444444444444444444 " + signResult); ByteBuffer
 * sign = signResult.getSignature();
 * System.out.println("55555555555555555555555555555 " + sign); String signedJwt
 * = new String(sign.array(), "UTF-8");
 * System.out.println("666666666666666666666666666666 " + signedJwt);
 * System.out.println(signedJwt);
 * 
 * //this.verifySgn(sign.array(), signData);
 * 
 * 
 * 
 * 
 * }
 * 
 * 
 * void verifySgn(String message) throws InvalidKeySpecException,
 * NoSuchAlgorithmException, SignatureException, IOException,
 * InvalidKeyException{ String base64EncodedKey =
 * "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApUPvCUlizUH53qHIW7VG\n" +
 * "96YjnfIhZ/x5B/ga5PFHH3gN5r0fqm0G9tMEuAakUe7Pdtq5M4SwJJSNOfu44/U+\n" +
 * "8KAT5nZ1rkjiJKdWmdWYmJBWXPz8Sr9H2dFglsGD07UhIpJWJZNT8+DcPiD10Ctl\n" +
 * "8KbR/o1pm6YxZTwMjdmgttcpGHaOvfbyPZZADY8032sOOXRevQsfrX5C6v3MQkzq\n" +
 * "FIhZaOsaL7ZK1Kkw2VOESq2b9P3GLEFu9dxcyajAtK7UeyRScgPED/FAWe7/31sA\n" +
 * "SQujw+rxirpkZD73PcSDBbH1cIf4oUQauU812SHKp7qZPQ5P8MKXxFCzuQdCHJdB\n" +
 * "DwIDAQAB";
 * 
 * byte[] pubKeyBytes = Base64.getMimeDecoder().decode(base64EncodedKey);
 * 
 * X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
 * 
 * PublicKey publicKey =
 * KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);
 * 
 * Signature sig = Signature.getInstance("SHA256withRSA");
 * 
 * sig.initVerify(publicKey);
 * 
 * sig.update(message.getBytes());
 * 
 * boolean signatureValid = sig.verify(getSignatureBytes());
 * System.out.println("***overridded message**********"+signatureValid); }
 * 
 * private byte[] getSignatureBytes() throws IOException {
 * 
 * Resource resource = new ClassPathResource("signsid.txt"); InputStream
 * inputStream = resource.getInputStream();
 * 
 * byte[] bdata = FileCopyUtils.copyToByteArray(inputStream); String s =
 * "ohFjHK3o/RiI5QNCtpsL8LUwxk6Aw9ni66qtxGfuJOckakwvQO2Cva5w9DQmkdo6H6ITMm7GqUYxhPtNgWrQSU1Frt9bDcPZVdxUg0clnbbI7cUMG+C2bsnUYn/3gdpwxcJbwnceFArLROhreb3XGb69NJV5shBw57TaoA1v9zjdmjvTG463i3ClQTRPJ4nKeqJkyifgcAx5otiWOSdrJFnDsIzGTIFN3GnvJSXwz5zs8ii9StCca8XlI0g02cRMaJDiJ6Ctzo2WlorttM8IfmyC8eqCnBc5FCOo1gxw+2gfCxC0jHHFcw3DFEmN+M8PhH2YhqKSkYZZKbA5//WgbQ==";
 * return Base64.getDecoder().decode(s); //return bdata; }
 * 
 * byte[] getSignBytes() throws IOException { FileInputStream fis = new
 * FileInputStream(new ClassPathResource("signsid.txt").getFile());
 * InputStreamReader is = new InputStreamReader(fis, "UTF-8");
 * 
 * BufferedReader in = new BufferedReader(is);
 * 
 * String line; String signdatas = ""; while ((line = in.readLine()) != null) {
 * System.out.println(line); signdatas += line.trim(); } in.close(); return
 * signdatas.getBytes(); }
 * 
 * private byte[] getRawMessage() throws IOException { Resource resource = new
 * ClassPathResource("message.txt"); InputStream inputStream =
 * resource.getInputStream();
 * 
 * byte[] bdata = FileCopyUtils.copyToByteArray(inputStream); return bdata; }
 * 
 *//**
	 * public RSAPrivateKey readPrivateKey(File file) throws Exception { String key
	 * = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
	 * 
	 * String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----",
	 * "").replaceAll(System.lineSeparator(), "") .replace("-----END PRIVATE
	 * KEY-----", "");
	 * 
	 * byte[] encoded = Base64.decodeBase64(privateKeyPEM);
	 * 
	 * KeyFactory keyFactory = KeyFactory.getInstance("RSA"); PKCS8EncodedKeySpec
	 * keySpec = new PKCS8EncodedKeySpec(encoded); return (RSAPrivateKey)
	 * keyFactory.generatePrivate(keySpec); }
	 **/
/*



*//**
	 * public static RSAPublicKey readPublicKey() throws Exception {
	 * 
	 * Resource resource = new
	 * ClassPathResource("Publickey-2d5c0b78-59aa-468e-91d5-b7e68dbbb42a.pem");
	 * InputStream inputStream = resource.getInputStream();
	 * 
	 * byte[] bdata = FileCopyUtils.copyToByteArray(inputStream); String key = new
	 * String(bdata, StandardCharsets.UTF_8); //String key = new
	 * String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
	 * 
	 * String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----",
	 * "").replaceAll(System.lineSeparator(), "") .replace("-----END PUBLIC
	 * KEY-----", "");
	 * 
	 * byte[] encoded = Base64.decodeBase64(publicKeyPEM);
	 * 
	 * KeyFactory keyFactory = KeyFactory.getInstance("RSA"); X509EncodedKeySpec
	 * keySpec = new X509EncodedKeySpec(encoded); return (RSAPublicKey)
	 * keyFactory.generatePublic(keySpec); }
	 **//*
		 * 
		 * 
		 * void verifySgn() throws InvalidKeySpecException, NoSuchAlgorithmException,
		 * SignatureException, IOException, InvalidKeyException{ String base64EncodedKey
		 * = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApUPvCUlizUH53qHIW7VG\n" +
		 * "96YjnfIhZ/x5B/ga5PFHH3gN5r0fqm0G9tMEuAakUe7Pdtq5M4SwJJSNOfu44/U+\n" +
		 * "8KAT5nZ1rkjiJKdWmdWYmJBWXPz8Sr9H2dFglsGD07UhIpJWJZNT8+DcPiD10Ctl\n" +
		 * "8KbR/o1pm6YxZTwMjdmgttcpGHaOvfbyPZZADY8032sOOXRevQsfrX5C6v3MQkzq\n" +
		 * "FIhZaOsaL7ZK1Kkw2VOESq2b9P3GLEFu9dxcyajAtK7UeyRScgPED/FAWe7/31sA\n" +
		 * "SQujw+rxirpkZD73PcSDBbH1cIf4oUQauU812SHKp7qZPQ5P8MKXxFCzuQdCHJdB\n" +
		 * "DwIDAQAB";
		 * 
		 * byte[] pubKeyBytes = Base64.getMimeDecoder().decode(base64EncodedKey);
		 * 
		 * X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
		 * 
		 * PublicKey publicKey =
		 * KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);
		 * 
		 * Signature sig = Signature.getInstance("SHA256withRSA");
		 * 
		 * sig.initVerify(publicKey);
		 * 
		 * sig.update("".getBytes());
		 * 
		 * boolean signatureValid = sig.verify(getSignatureBytes());
		 * System.out.println("****signverify result*********"+signatureValid); }
		 * 
		 * 
		 * void verify() throws InvalidKeyException, Exception{ Signature sig =
		 * Signature.getInstance("SHA512withRSA");
		 * 
		 * //sig.initVerify(readPublicKey());
		 * 
		 * sig.update(getRawMessage());
		 * 
		 * boolean signatureValid = sig.verify(getSignatureBytes());
		 * System.out.println(signatureValid);
		 * 
		 * }
		 * 
		 * 
		 * private Certificate loadCertificate(String filename) throws
		 * FileNotFoundException, CertificateException { FileInputStream fis = new
		 * FileInputStream(filename); BufferedInputStream bis = new
		 * BufferedInputStream(fis); CertificateFactory cf =
		 * CertificateFactory.getInstance("X.509"); return cf.generateCertificate(bis);
		 * }
		 * 
		 * 
		 * public static void main(String[] args) throws Exception { HelloController2 as
		 * = new HelloController2(); as.verifySgn("test data1"); }
		 * 
		 * 
		 * 
		 * }
		 */