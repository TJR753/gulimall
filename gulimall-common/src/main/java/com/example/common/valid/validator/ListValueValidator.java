package com.example.common.valid.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;

public class ListValueValidator implements ConstraintValidator<ListValue,Integer> {
    HashSet<Integer> set=new HashSet<>();
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }

    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for (int i:vals){
            set.add(i);
        }
    }
}
