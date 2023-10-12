package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import childrencare.app.ChildrenCareApplication;
import childrencare.app.model.DrugModel;
import childrencare.app.service.DrugService;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { ChildrenCareApplication.class })
class ChildrenCareApplicationTests {
	@Autowired
	private DrugService drugService;

	@Test
    public void testGetAllDrugs() {
       /*  List<DrugModel> drugModels = drugService.findAllDrugs();
        // Use assertions to check if the result is as expected
        assertEquals(drugModels.size(), 3); */
    }

}
