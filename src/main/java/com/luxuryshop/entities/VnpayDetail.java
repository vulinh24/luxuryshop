package com.luxuryshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vnpay_detail")
public class VnpayDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "vnp_amount", nullable = true)
    private String vnp_Amount;
    @Column(name = "vnp_bank_code", nullable = true)
    private String vnp_BankCode;
    @Column(name = "vnp_bank_tranno", nullable = true)
    private String vnp_BankTranNo;
    @Column(name = "vnp_pay_date", nullable = true)
    private String vnp_PayDate;
    @Column(name = "vnp_order_info", nullable = true)
    private String vnp_OrderInfo;
    @Column(name = "vnp_transaction_no", nullable = true)
    private String vnp_TransactionNo;
    @Column(name = "vnp_response_code", nullable = true)
    private String vnp_ResponseCode;
    @Column(name = "vnp_transaction_status", nullable = true)
    private String vnp_TransactionStatus;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
}
