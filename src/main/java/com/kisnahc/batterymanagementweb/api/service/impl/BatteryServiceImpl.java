package com.kisnahc.batterymanagementweb.api.service.impl;

import com.kisnahc.batterymanagementweb.api.domain.Battery;
import com.kisnahc.batterymanagementweb.api.dto.request.CreateBatteryRequest;
import com.kisnahc.batterymanagementweb.api.dto.request.UpdateBatteryRequest;
import com.kisnahc.batterymanagementweb.api.dto.response.*;
import com.kisnahc.batterymanagementweb.api.exceprion.BatteryNotFoundException;
import com.kisnahc.batterymanagementweb.api.repository.BatteryRepository;
import com.kisnahc.batterymanagementweb.api.service.BatteryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BatteryServiceImpl implements BatteryService {

    private final BatteryRepository batteryRepository;

    @Transactional
    @Override
    public ApiResponse<CreateBatteryResponse> create(CreateBatteryRequest request) {

        Battery battery = Battery.builder()
                .name(request.getName())
                .voltage(request.getVoltage())
                .type(request.getType())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();

        Battery saveBattery = batteryRepository.save(battery);

        return new ApiResponse<>(SC_CREATED, new CreateBatteryResponse(saveBattery));
    }

    @Override
    public ApiResponse<BatteryResponse> get(Long batteryId) {

        Battery battery = batteryRepository.findById(batteryId)
                .orElseThrow(BatteryNotFoundException::new);

        return new ApiResponse<>(SC_OK, new BatteryResponse(battery));
    }

    @Override
    public ApiResponse<List<BatteryResponse>> getAll() {

        List<Battery> batteries = batteryRepository.findAll();

        List<BatteryResponse> batteryResponses = batteries.stream()
                .map(BatteryResponse::new)
                .collect(Collectors.toList());

        return new ApiResponse<>(SC_OK, batteryResponses.size() , batteryResponses);
    }

    @Transactional
    @Override
    public ApiResponse<UpdateBatteryResponse> update(Long batteryId, UpdateBatteryRequest request) {

        Battery battery = batteryRepository.findById(batteryId)
                .orElseThrow(BatteryNotFoundException::new);

        battery.update(request);

        return new ApiResponse<>(SC_OK, new UpdateBatteryResponse(battery));
    }

    @Transactional
    @Override
    public ApiResponse<DeleteBatteryResponse> delete(Long batteryId) {
        Battery battery = batteryRepository.findById(batteryId)
                .orElseThrow(BatteryNotFoundException::new);

        batteryRepository.delete(battery);

        return new ApiResponse<>(SC_OK, new DeleteBatteryResponse(battery));
    }
}