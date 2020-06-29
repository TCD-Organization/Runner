package com.example.core.resources;

import com.example.core.DTO.dataDTO;
import com.example.core.models.Analysis;
import com.example.core.models.Data;
import com.example.core.models.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/status")
public class RunnerResource {

    @GetMapping
    public ResponseEntity<HttpStatus> status() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
