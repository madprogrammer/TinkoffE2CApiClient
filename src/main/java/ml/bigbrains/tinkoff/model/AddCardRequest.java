package ml.bigbrains.tinkoff.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.Map;

public class AddCardRequest extends SignedCustomerKeyRequest {
    @JsonProperty("CheckType")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String checkType;

    @JsonProperty("Description")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;

    @JsonProperty("PayForm")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String payForm;

    @JsonCreator
    public AddCardRequest(
            @JsonProperty(value = "CustomerKey", required = true) String customerKey,
            @JsonProperty(value = "TerminalKey", required = true) String terminalKey
    ) {
        super(customerKey, terminalKey);
    }

    @Override
    protected Map<String, String> getMapForSignature() {
        Map<String, String> data = new HashMap<>();
        if(StringUtils.isNotEmpty(checkType))
            data.put("CheckType", checkType);
        if(StringUtils.isNotEmpty(description))
            data.put("Description", description);
        if(StringUtils.isNotEmpty(payForm))
            data.put("PayForm", payForm);

        data.putAll(super.getMapForSignature());
        return data;
    }

    @Override
    public String getUri() {
        return "/e2c/AddCard";
    }
}
