package ml.bigbrains.tinkoff.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

public class RemoveCustomerRequest extends SignedCustomerKeyRequest {

    @JsonCreator
    public RemoveCustomerRequest(
            @JsonProperty(value = "CustomerKey", required = true) String customerKey,
            @JsonProperty(value = "TerminalKey", required = true) String terminalKey
    ) {
        super(customerKey, terminalKey);
    }

    @Override
    public String getUri() {
        return "/RemoveCustomer";
    }

}
