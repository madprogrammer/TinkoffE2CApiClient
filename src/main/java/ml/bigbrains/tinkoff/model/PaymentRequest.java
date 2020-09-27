package ml.bigbrains.tinkoff.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentRequest extends SignedRequest {
    @JsonProperty("PaymentId")
    private Long paymentId;

    @JsonCreator
    public PaymentRequest(
            @JsonProperty(value = "PaymentId", required = true) Long paymentId,
            @JsonProperty(value = "TerminalKey", required = true) String terminalKey
    ) {
        super(terminalKey);
        this.paymentId = paymentId;
    }

    @Override
    protected Map<String, String> getMapForSignature() {
        Map<String,String> data = new HashMap<>();
        if(StringUtils.isNotEmpty(String.valueOf(paymentId)))
            data.put("PaymentId", String.valueOf(paymentId));

        data.putAll(super.getMapForSignature());
        return data;
    }

    @Override
    public String getUri() {
        return "/e2c/Payment";
    }
}
