package com.identifix.duplicatepagesxmlfile.controller

import com.identifix.duplicatepagesxmlfile.proposal.DuplicatePages
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@Component
@Slf4j
@RequestMapping("/run")
class DuplicatePagesController {
    @Autowired
    DuplicatePages duplicatePages

    @PostMapping("/read")
    ResponseEntity<String> sendMessages(@RequestBody String files) {
        String response = duplicatePages.readXmlFiles(files)
        new ResponseEntity(response, HttpStatus.ACCEPTED)
    }
}
