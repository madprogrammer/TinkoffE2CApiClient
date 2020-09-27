package ml.bigbrains.tinkoff.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class InitRequest extends SignedRequest {
    @JsonProperty("OrderId")
    private String orderId;

    @JsonProperty("CardId")
    private String cardId;

    @JsonProperty("Amount")
    private Long amount;

    // ISO 4217 Code
    @JsonProperty("Currency")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer currency;

    @JsonProperty("CustomerKey")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String customerKey;

    @JsonProperty("IP")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String ip;

    @JsonProperty("DATA")
    private String data;

    @JsonCreator
    public InitRequest(
            @JsonProperty(value = "OrderId", required = true) String orderId,
            @JsonProperty(value = "CardId", required = true) String cardId,
            @JsonProperty(value = "Amount", required = true) Long amount,
            @JsonProperty(value = "TerminalKey", required = true) String terminalKey
    ) {
        super(terminalKey);
        this.orderId = orderId;
        this.cardId = cardId;
        this.amount = amount;
    }

    @Override
    protected Map<String, String> getMapForSignature() {
        Map<String, String> data = new HashMap<>();
        if(StringUtils.isNotEmpty(orderId))
            data.put("OrderId", orderId);
        if(StringUtils.isNotEmpty(cardId))
            data.put("CardId", cardId);
        if(StringUtils.isNotEmpty(String.valueOf(amount)))
            data.put("Amount", String.valueOf(amount));
        if(currency != null)
            data.put("Currency", String.valueOf(currency));
        if(StringUtils.isNotEmpty(customerKey))
            data.put("CustomerKey", customerKey);
        if(StringUtils.isNotEmpty(ip))
            data.put("IP", ip);
        if(StringUtils.isNotEmpty(this.data))
            data.put("DATA", this.data);

        data.putAll(super.getMapForSignature());
        return data;
    }

    @Override
    public String getUri() {
        return "/e2c/Init";
    }
}
