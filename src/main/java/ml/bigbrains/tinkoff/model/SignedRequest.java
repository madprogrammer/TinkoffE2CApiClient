package ml.bigbrains.tinkoff.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ml.bigbrains.tinkoff.CryptographicException;
import org.apache.commons.lang3.StringUtils;
import ru.CryptoPro.JCP.JCP;
import ru.tinkoff.crypto.mapi.CryptoMapi;

@Slf4j
@Getter
public abstract class SignedRequest {
    @JsonProperty("TerminalKey")
    private String terminalKey;

    @JsonProperty("DigestValue")
    private String digestValue;

    @JsonProperty("SignatureValue")
    private String signatureValue;

    @JsonProperty("X509SerialNumber")
    private String x509SerialNumber;

    @JsonIgnore
    protected final ObjectMapper objectMapper;

    @JsonCreator
    protected SignedRequest(
            @JsonProperty(value = "TerminalKey", required = true) String terminalKey
    ) {
        this();
        this.terminalKey = terminalKey;
    }

    protected SignedRequest() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public void sign(String keyStoreInstanceName, String keyName, String keyPassword, String x509Serial)
            throws CryptographicException
    {
        try {
            Security.addProvider(new JCP());

            CryptoMapi crypto = new CryptoMapi();
            String data = crypto.concatValues(this.getMapForSignature());

            byte[] digestData = crypto.calcDigest(JCP.GOST_DIGEST_2012_256_NAME, data.getBytes(StandardCharsets.UTF_8));

            KeyStore store = KeyStore.getInstance(keyStoreInstanceName);
            store.load(null, null);

            Key privateKey = store.getKey(keyName, keyPassword == null ? null : keyPassword.toCharArray());
            String signatureAlgorithm = JCP.GOST_SIGN_2012_256_NAME;

            log.debug("Private key algorithm: " + privateKey.getAlgorithm());
            if (privateKey.getAlgorithm().equals(JCP.GOST_DH_2012_512_NAME))
                signatureAlgorithm = JCP.GOST_SIGN_2012_512_NAME;

            byte[] signature = crypto.calcSignature(signatureAlgorithm, (PrivateKey) privateKey, digestData);

            this.digestValue = Base64.getEncoder().encodeToString(digestData);
            this.signatureValue = Base64.getEncoder().encodeToString(signature);
            this.x509SerialNumber = x509Serial;
        } catch (Exception exc) {
            throw new CryptographicException(exc);
        }
    }

    @JsonIgnore
    protected Map<String, String> getMapForSignature() {
        Map<String, String> data = new HashMap<>();
        if(StringUtils.isNotEmpty(terminalKey))
            data.put("TerminalKey", terminalKey);
        return data;
    }

    @JsonIgnore
    public Map<String, String> toMap() {
        return objectMapper.convertValue(this, new TypeReference<Map<String, String>>() {});
    }

    @JsonIgnore
    public abstract String getUri();
}
