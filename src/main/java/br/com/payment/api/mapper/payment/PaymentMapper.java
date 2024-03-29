package br.com.payment.api.mapper.payment;

import br.com.payment.api.model.dto.payment.request.PaymentDTO;
import br.com.payment.api.model.dto.payment.response.PaymentResponseDTO;
import br.com.payment.api.model.entity.payment.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {

  PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

  @Mapping(source = "date", target = "creationDate")
  Payment toPayment(PaymentDTO PaymentDTO);

  PaymentDTO toDTO(Payment Payment);

}
