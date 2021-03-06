package com.example.packingapp.model.GetOrderResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetOrderData {

    @SerializedName("order_number")
    private String order_number;

    @SerializedName("customer")
    private customer customer;

    @SerializedName("delivery")
    private delivery delivery;

    @SerializedName("grand_total")
    private String grand_total;

    @SerializedName("shipping_fees")
    private float shipping_fees;

    @SerializedName("picker_confirmation_time")
    private String picker_confirmation_time;

    @SerializedName("currency")
    private String currency;

    @SerializedName("status")
    private String Status;

    @SerializedName("outbound_delivery")
    private String OutBound_delivery;

    @SerializedName("out_from_site")
    private String Out_From_Loc;

    @SerializedName("items")
    private List<ItemsOrderDataDBDetails> itemsOrderDataDBDetails;

    @SerializedName("reedemed_points_amount")
    private String reedemed_points_amount;

    @SerializedName("payment_method")
    private String payment_method;

    public String getPayment_method() {

        if (payment_method.equals("cashondelivery"))
        {
            return "الدفع عند الاستلام";
        }
        else if (payment_method.equals("cardondelivery"))
        {
            return "البطاقة عند الاستلام";
        }
        else if (payment_method.equals("robusta_accept_cc"))
        {
            return "بطاقة الإئتمان";
        }
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getReedemed_points_amount() {
        return reedemed_points_amount;
    }

    public void setReedemed_points_amount(String reedemed_points_amount) {
        this.reedemed_points_amount = reedemed_points_amount;
    }

    public String getOutBound_delivery() {
        return OutBound_delivery;
    }

    public void setOutBound_delivery(String outBound_delivery) {
        OutBound_delivery = outBound_delivery;
    }

    public String getPicker_confirmation_time() {
        return picker_confirmation_time;
    }

    public void setPicker_confirmation_time(String picker_confirmation_time) {
        this.picker_confirmation_time = picker_confirmation_time;
    }

    public float getShipping_fees() {
        return shipping_fees;
    }

    public void setShipping_fees(float shipping_fees) {
        this.shipping_fees = shipping_fees;
    }

//    @SerializedName("Out_From_Loc")
//    private String Out_From_Loc;


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public customer getCustomer() {
        return customer;
    }

    public void setCustomer(customer customer) {
        this.customer = customer;
    }

    public delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(delivery delivery) {
        this.delivery = delivery;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getOut_From_Loc() {
        return Out_From_Loc;
    }

    public void setOut_From_Loc(String out_From_Loc) {
        Out_From_Loc = out_From_Loc;
    }

    public List<ItemsOrderDataDBDetails> getItemsOrderDataDBDetails() {
        return itemsOrderDataDBDetails;
    }

    public void setItemsOrderDataDBDetails(List<ItemsOrderDataDBDetails> itemsOrderDataDBDetails) {
        this.itemsOrderDataDBDetails = itemsOrderDataDBDetails;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


}
