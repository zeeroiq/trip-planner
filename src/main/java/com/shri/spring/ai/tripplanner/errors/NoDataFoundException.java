package com.shri.spring.ai.tripplanner.errors;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoDataFoundException extends RuntimeException {
    String message;
    int code;
}
