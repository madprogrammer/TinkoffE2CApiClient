package ml.bigbrains.tinkoff.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class SignedCustomerKeyRequest extends SignedRequest {
    @JsonProperty("CustomerKey")
    private String customerKey;

    @JsonProperty("IP")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String ip;

    @JsonCreator
    protected SignedCustomerKeyRequest(
            @JsonProperty(value = "CustomerKey", required = true) String customerKey,
            String terminalKey) {
        super(terminalKey);
        this.customerKey = customerKey;
    }

    @Override
    protected Map<String, String> getMapForSignature() {
        Map<String, String> data = new HashMap<>();
        if(StringUtils.isNotEmpty(customerKey))
            data.put("CustomerKey", customerKey);
        if(StringUtils.isNotEmpty(ip))
            data.put("IP", ip);

        data.putAll(super.getMapForSignature());
        return data;
    }
}
