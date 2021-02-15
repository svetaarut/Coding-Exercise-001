package com.transunion.test.codingexercise.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Builder
@Getter
@Setter
public class SoundexResponse {
    List<String> output;
}
