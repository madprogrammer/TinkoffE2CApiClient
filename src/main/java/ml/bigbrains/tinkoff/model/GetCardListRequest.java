package ml.bigbrains.tinkoff.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetCardListRequest extends SignedCustomerKeyRequest {

    @JsonCreator
    public GetCardListRequest(
            @JsonProperty(value = "CustomerKey", required = true) String customerKey,
            @JsonProperty(value = "TerminalKey", required = true) String terminalKey
    ) {
        super(customerKey, terminalKey);
    }

    @Override
    public String getUri() {
        return "/GetCardList";
    }
}
