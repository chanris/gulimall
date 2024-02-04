package com.chanris.gulimall.product;

import io.swagger.models.auth.In;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chenyue7@foxmail.com
 * @date 4/2/2024
 * @description
 */
@SpringBootTest
public class HibernateValidationTests {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void test01() {
        Car car = new Car(null, "DD-AB-123", 1);
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

        System.out.println(constraintViolations.size());

        // 违法约束的字段字段个数，以及message
        Iterator<ConstraintViolation<Car>> iterator = constraintViolations.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<Car> next = iterator.next();
            System.out.println(next.getMessage());
        }

    }

    static class Car {
        @NotNull
        private String manufacturer;

        @NotNull
        @Size(min = 2, max = 4)
        private String licensePlate;

        @Min(2)
        private Integer seatCount;

        public Car(String manufacturer, String licensePlate, Integer seatCount) {
            this.manufacturer = manufacturer;
            this.licensePlate = licensePlate;
            this.seatCount = seatCount;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public void setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
        }

        public Integer getSeatCount() {
            return seatCount;
        }

        public void setSeatCount(Integer seatCount) {
            this.seatCount = seatCount;
        }
    }
}
