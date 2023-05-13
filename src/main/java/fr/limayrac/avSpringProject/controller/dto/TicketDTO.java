package fr.limayrac.avSpringProject.controller.dto;

import com.sun.istack.NotNull;

import java.math.BigDecimal;

public class TicketDTO {

    @NotNull
    private String type;

    @NotNull
    private BigDecimal price;

    private Long event;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getEvent() {
        return event;
    }

    public void setEvent(Long event) {
        this.event = event;
    }
}
