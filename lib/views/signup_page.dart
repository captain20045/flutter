import 'package:flutter/material.dart';
import 'package:http/http.dart' as http; //http 요청을 보내기위한 import 문
import 'package:flutter_project/models/user_model.dart'; // 'SignUpModel'이 정의된 파일을 import 합니다.
import 'dart:convert';
import 'main_page.dart'; // MainPage 위젯을 import합니다.

class SignUpPage extends StatelessWidget {
  // TextEditingController 인스턴스 생성
  final nameController = TextEditingController();
  final genderController = TextEditingController();
  final userIdController = TextEditingController();
  final passwordController = TextEditingController();
  final emailController = TextEditingController();
  final phoneController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('회원가입')),
      body: Padding(
        padding: EdgeInsets.all(20.0),
        child: Column(
          children: <Widget>[
            TextField(
              controller: nameController,
              decoration: InputDecoration(labelText: '이름'),
            ),
            TextField(
              controller: genderController,
              decoration: InputDecoration(labelText: '성별'),
            ),
            TextField(
              controller: userIdController,
              decoration: InputDecoration(labelText: '아이디'),
            ),
            TextField(
              controller: passwordController,
              decoration: InputDecoration(labelText: '비밀번호'),
              obscureText: true,
            ),
            TextField(
              controller: emailController,
              decoration: InputDecoration(labelText: '이메일'),
            ),
            TextField(
              controller: phoneController,
              decoration: InputDecoration(labelText: '전화번호'),
            ),
            ElevatedButton(
              onPressed: () async {
                // 사용자 입력을 이용해 SignUpModel 객체 생성
                SignUpModel signUpModel = SignUpModel(
                  name: nameController.text,
                  gender: genderController.text,
                  userid: userIdController.text,
                  userpw: passwordController.text,
                  email: emailController.text,
                  phone_number: phoneController.text,
                );

                var url = Uri.parse('http://localhost:8080/api/registerAction');
                var response = await http.post(url,
                    headers: {"Content-Type": "application/json"},
                    body: json.encode(
                        signUpModel.toJson())); // toJson() 메서드를 이용해 JSON으로 변환

                if (response.statusCode == 200) {
                  print('회원가입 성공: ${response.body}');
                  Navigator.pushReplacement(context,
                      MaterialPageRoute(builder: (context) => MainPage()));
                } else {
                  print('회원가입 실패: ${response.statusCode}');
                }
              },
              child: Text('회원가입'),
            )
          ],
        ),
      ),
    );
  }
}
