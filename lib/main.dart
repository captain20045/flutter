import 'package:flutter/material.dart';
import 'views/login_page.dart';
import 'views/signup_page.dart';
import 'views/main_page.dart';
import 'views/mypage_page.dart'; // 여기에 추가

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '영화 추천 앱',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MainPage(),
      routes: {
        '/login': (context) => LoginPage(),
        '/signup': (context) => SignUpPage(),
        '/mypage': (context) => MyPage(), // 여기에 추가
      },
    );
  }
}
