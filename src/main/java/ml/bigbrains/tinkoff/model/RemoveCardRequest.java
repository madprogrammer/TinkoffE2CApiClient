package ml.bigbrains.tinkoff.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class RemoveCardRequest extends SignedCustomerKeyRequest {
    @JsonProperty("CardId")
    private Long cardId;

    @JsonCreator
    public RemoveCardRequest(
            @JsonProperty(value = "CardId", required = true) Long cardId,
            @JsonProperty(value = "CustomerKey", required = true) String customerKey,
            @JsonProperty(value = "TerminalKey", required = true) String terminalKey
    )
    {
        super(customerKey, terminalKey);
        this.cardId = cardId;
    }

    @Override
    protected Map<String, String> getMapForSignature() {
        Map<String,String> data = new HashMap<>();
        if(StringUtils.isNotEmpty(String.valueOf(cardId)))
            data.put("CardId", String.valueOf(cardId));

        data.putAll(super.getMapForSignature());
        return data;
    }

    @Override
    public String getUri() {
        return "/RemoveCard";
    }
}
