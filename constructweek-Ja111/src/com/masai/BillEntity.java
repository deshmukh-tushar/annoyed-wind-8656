package com.masai;



import java.time.LocalDate;

public class BillEntity {
    Status status;
    static final Double fixedChargeForConnection = 49.00;
   public static final Double perUnitCost = 10.00;
    Double unitsConsumed;
    Double taxes;
    Double adjustment;
    Double total;
    LocalDate date;
    public BillEntity() {
        this.status = Status.PENDING;
    }

    public BillEntity(Double unitsConsumed, Double taxes, Double adjustment, Double total) {
        this.status = Status.PENDING;
        this.unitsConsumed = unitsConsumed;
        this.taxes = taxes;
        this.adjustment = adjustment;
        this.total = total;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getFixedChargeForConnection() {
        return fixedChargeForConnection;
    }

    public Double getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(Double unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public Double getTaxes() {
        return taxes;
    }

    public void setTaxes(Double taxes) {
        this.taxes = taxes;
    }

    public Double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Double adjustment) {
        this.adjustment = adjustment;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}

