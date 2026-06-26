package com.ShopSphere.e_commerce.Controller;

import com.ShopSphere.e_commerce.Service.AddressService;
import com.ShopSphere.e_commerce.dto.AddressRequestDto;
import com.ShopSphere.e_commerce.dto.AddressResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressResponseDto> addAddress(@Valid @RequestBody AddressRequestDto addressRequestDto){
        return  ResponseEntity.ok(addressService.addAddress(addressRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDto>> getAllAddresses(){
        return ResponseEntity.ok(addressService.getMyAddresses());
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDto> updateAddress(@PathVariable Long addressId,
                                            @Valid @RequestBody AddressRequestDto addressRequestDto){

        return ResponseEntity.ok(addressService.updateAddress(addressId, addressRequestDto));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok("Address deleted successfully");
    }
}
