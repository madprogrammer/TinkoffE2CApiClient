package ml.bigbrains.tinkoff.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonCreator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class GetStateRequest extends SignedRequest {
    @JsonProperty("PaymentId")
    private Long paymentId;

    @JsonProperty("IP")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String ip;

    @JsonCreator
    public GetStateRequest(
            @JsonProperty(value = "PaymentId", required = true) Long paymentId,
            @JsonProperty(value = "TerminalKey", required = true) String terminalKey
    ) {
        super(terminalKey);
        this.paymentId = paymentId;
    }

    @Override
    protected Map<String, String> getMapForSignature() {
        Map<String, String> data = new HashMap<>();
        if(StringUtils.isNotEmpty(String.valueOf(paymentId)))
            data.put("PaymentId", String.valueOf(paymentId));
        if(StringUtils.isNotEmpty(ip))
            data.put("IP", ip);

        data.putAll(super.getMapForSignature());
        return data;
    }

    @Override
    public String getUri() {
        return "/GetState";
    }
}
