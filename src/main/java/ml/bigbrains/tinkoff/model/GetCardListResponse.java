package ml.bigbrains.tinkoff.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetCardListResponse {
    @JsonProperty("Pan")
    private String pan;

    @JsonProperty("CardId")
    private String cardId;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("RebillId")
    private Long rebillId;

    @JsonProperty("CardType")
    private Integer cardType;

    @JsonProperty("ExpDate")
    private String expDate;
}
