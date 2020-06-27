package com.example.ajeetseeds.ui.order_creation.order_approval;

import java.util.List;

public class OrderApprovalModel {
    public boolean condition;
    public String message;
    public String order_no;
    public String approver_email;
    public String user_type;
    public String customer_no;
    public String order_status;
    public String sum_of_qty;
    public String updated_on;
    public List<OrderLineModel> orderline;

    public class OrderLineModel {
        public String order_no;
        public String item_no;
        public String item_name;
        public String image_url;
        public String qty;
        public String updated_on;
    }
}
