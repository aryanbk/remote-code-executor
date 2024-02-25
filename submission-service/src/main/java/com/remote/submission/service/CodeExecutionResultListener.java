// package com.remote.submission.service;

// import org.springframework.amqp.rabbit.annotation.RabbitListener;
// import org.springframework.stereotype.Service;

// import com.remote.submission.config.Constants;
// import com.remote.submission.model.CodeExecutionResult;

// @Service
// public class CodeExecutionResultListener {
// // @Autowired
// // private CodeExecutionResultRepository resultRepository;

// @RabbitListener(queues = Constants.CODE_EXECUTION_RESULT_QUEUE)
// public void receiveCodeExecutionResult(CodeExecutionResult result) {
// // resultRepository.save(result);
// System.out.println("Received code execution result: " + result);
// }

// }