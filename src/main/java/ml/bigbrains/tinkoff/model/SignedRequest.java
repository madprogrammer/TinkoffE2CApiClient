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
import ml.bigbrains.tinkoff.CryptoHelper;
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
        CryptoHelper cryptoHelper = new CryptoHelper();
        String data = cryptoHelper.getCryptoMapi().concatValues(this.getMapForSignature());
        cryptoHelper.sign(data, keyStoreInstanceName, keyName, keyPassword);

        this.signatureValue = cryptoHelper.getSignatureValue();
        this.digestValue = cryptoHelper.getDigestValue();
        this.x509SerialNumber = x509Serial;
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
