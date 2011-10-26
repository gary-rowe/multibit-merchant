import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * * <p>Utility to provide the following to developers:<br>
 * * <ul>
 * * <li>Quick tool to import the self-signed certificate from localhost:8443 into the local trust store</li>
 * * </ul>
 * * Example:
 * * <pre>
 * * java InstallCert localhost:8443 jetty6
 * * </pre>
 * * </p>
 */

public class InstallCert {

  public static void main(String[] args) throws Exception {
    String host;
    int port;
    char[] passphrase;
    if ((args.length == 1) || (args.length == 2)) {
      String[] c = args[0].split(":");
      host = c[0];
      port = (c.length == 1) ? 443 : Integer.parseInt(c[1]);
      String p = (args.length == 1) ? "changeit" : args[1];
      passphrase = p.toCharArray();
    } else {
      System.out.println("Usage: java InstallCert <host>[:port] [passphrase]");
      return;
    }

    char SEP = File.separatorChar;

    File file = new File("jssecacerts");
    File dir = new File(System.getProperty("java.home") + SEP
      + "lib" + SEP + "security");
    if (!file.isFile()) {
      file = new File(dir, "jssecacerts");
      if (!file.isFile()) {
        file = new File(dir, "cacerts");
      }
    }
    System.out.println("Loading KeyStore " + file + "...");
    InputStream in = new FileInputStream(file);
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(in, passphrase);
    in.close();

    SSLContext context = SSLContext.getInstance("TLS");
    TrustManagerFactory tmf =
      TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ks);
    X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
    SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
    context.init(null, new TrustManager[]{tm}, null);
    SSLSocketFactory factory = context.getSocketFactory();

    System.out.println("Opening connection to " + host + ":" + port + "...");
    SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
    socket.setSoTimeout(10000);
    try {
      System.out.println("Starting SSL handshake...");
      socket.startHandshake();
      socket.close();
      System.out.println("=============================================");
      System.out.println("No errors. The certificate is already trusted");
      System.out.println("=============================================");
    } catch (SSLException e) {
      System.out.println("========================================================");
      System.out.println("PKIX errors detected. You need to import the certificate");
      System.out.println("========================================================");
      //e.printStackTrace(System.out);
    }


    X509Certificate[] chain = tm.chain;
    if (chain == null) {
      System.out.println("Could not obtain server certificate chain");
      return;
    }

    BufferedReader reader =
      new BufferedReader(new InputStreamReader(System.in));

    System.out.println();

    System.out.println("Server sent " + chain.length + " certificate(s) which you can now add to the trust store:");
    System.out.println();
    MessageDigest sha1 = MessageDigest.getInstance("SHA1");
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    for (int i = 0; i < chain.length; i++) {
      X509Certificate cert = chain[i];
      System.out.println
        (" " + (i + 1) + " Subject " + cert.getSubjectDN());
      System.out.println(" Issuer " + cert.getIssuerDN());
      sha1.update(cert.getEncoded());
      System.out.println(" sha1 " + toHexString(sha1.digest()));
      md5.update(cert.getEncoded());
      System.out.println(" md5 " + toHexString(md5.digest()));
      System.out.println();
    }

    System.out.println("Enter certificate number to add to trusted keystore or 'q' to quit: [1]");
    String line = reader.readLine().trim();
    int k;
    try {
      k = (line.length() == 0) ? 0 : Integer.parseInt(line) - 1;
    } catch (NumberFormatException e) {
      System.out.println("KeyStore not changed");
      return;
    }

    X509Certificate cert = chain[k];
    String alias = host + "-" + (k + 1);
    ks.setCertificateEntry(alias, cert);

    System.out.println("Adding JSSE certificates under 'java.home': " + dir.getAbsolutePath() + "\\jssecacerts");
    OutputStream out = new FileOutputStream(System.getProperty("java.home") + SEP
      + "lib" + SEP + "security" + SEP + "jssecacerts");
    ks.store(out, passphrase);
    out.close();

    System.out.println();
    System.out.println(cert);
    System.out.println();

    System.out.println("Added certificate to trusted keystore using alias '" + alias + "'. Your SSL is now configured.");
  }


  private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();


  private static String toHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 3);
    for (int b : bytes) {
      b &= 0xff;
      sb.append(HEXDIGITS[b >> 4]);
      sb.append(HEXDIGITS[b & 15]);
      sb.append(' ');
    }
    return sb.toString();
  }


  private static class SavingTrustManager implements X509TrustManager {


    private final X509TrustManager tm;

    private X509Certificate[] chain;


    SavingTrustManager(X509TrustManager tm) {
      this.tm = tm;
    }


    public X509Certificate[] getAcceptedIssuers() {
      throw new UnsupportedOperationException();
    }


    public void checkClientTrusted(X509Certificate[] chain, String authType)

      throws CertificateException

    {
      throw new UnsupportedOperationException();
    }


    public void checkServerTrusted(X509Certificate[] chain, String authType)

      throws CertificateException

    {
      this.chain = chain;
      tm.checkServerTrusted(chain, authType);
    }


  }

}
