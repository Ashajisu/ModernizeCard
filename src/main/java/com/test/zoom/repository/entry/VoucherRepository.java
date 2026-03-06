package com.test.zoom.repository.entry;

import com.test.zoom.dto.VoucherSelectDto;
import com.test.zoom.entity.entry.Voucher;
import com.test.zoom.entity.entry.VoucherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query(value = "CALL ledger.getVoucherList(:start,:end,:type)", nativeQuery = true)
    List<VoucherSelectDto> getVoucherList(LocalDate start, LocalDate end, String type);
}
