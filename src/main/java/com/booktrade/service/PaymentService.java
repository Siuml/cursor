package com.booktrade.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.booktrade.entity.Payment;
import com.booktrade.entity.TradeOrder;
import com.booktrade.mapper.PaymentMapper;
import com.booktrade.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    public PaymentService(PaymentMapper paymentMapper, OrderMapper orderMapper) {
        this.paymentMapper = paymentMapper;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public boolean pay(Long orderId, BigDecimal amount) {
        return pay(orderId, amount, "card");
    }

    @Transactional
    public boolean pay(Long orderId, BigDecimal amount, String payMethod) {
        TradeOrder order = orderMapper.selectById(orderId);
        if (order == null || order.getStatus() != 1) {
            return false;
        }

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPayMethod(payMethod);
        payment.setPayTime(LocalDateTime.now());
        paymentMapper.insert(payment);

        TradeOrder updateOrder = new TradeOrder();
        updateOrder.setId(orderId);
        updateOrder.setStatus(4);
        orderMapper.updateById(updateOrder);

        return true;
    }

    public Payment getByOrderId(Long orderId) {
        return paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>().eq(Payment::getOrderId, orderId));
    }

    public boolean isPaid(Long orderId) {
        return paymentMapper.selectCount(
                new LambdaQueryWrapper<Payment>().eq(Payment::getOrderId, orderId)) > 0;
    }
}