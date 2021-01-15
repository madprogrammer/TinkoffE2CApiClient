package ml.bigbrains.tinkoff;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.CryptoPro.JCP.JCP;
import ru.tinkoff.crypto.mapi.CryptoMapi;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Base64;

@Getter
public class CryptoHelper {
    @JsonProperty("DigestValue")
    private String digestValue;

    @JsonProperty("SignatureValue")
    private String signatureValue;

    @JsonIgnore
    private final CryptoMapi cryptoMapi;

    public CryptoHelper() {
        Security.addProvider(new JCP());
        this.cryptoMapi = new CryptoMapi();
    }

    public void sign(String data, String keyStoreInstanceName, String keyName, String keyPassword)
            throws CryptographicException
    {
        try {
            byte[] digestData = this.cryptoMapi.calcDigest(JCP.GOST_DIGEST_2012_256_NAME, data.getBytes(StandardCharsets.UTF_8));

            KeyStore store = KeyStore.getInstance(keyStoreInstanceName);
            store.load(null, null);

            Key privateKey = store.getKey(keyName, keyPassword == null ? null : keyPassword.toCharArray());
            String signatureAlgorithm = JCP.GOST_SIGN_2012_256_NAME;

            if (privateKey.getAlgorithm().equals(JCP.GOST_DH_2012_512_NAME))
                signatureAlgorithm = JCP.GOST_SIGN_2012_512_NAME;

            byte[] signature = this.cryptoMapi.calcSignature(signatureAlgorithm, (PrivateKey) privateKey, digestData);

            this.digestValue = Base64.getEncoder().encodeToString(digestData);
            this.signatureValue = Base64.getEncoder().encodeToString(signature);
        } catch (Exception exc) {
            throw new CryptographicException(exc);
        }
    }
}
