import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import 'mypage_page.dart'; // MainPage 위젯을 import합니다.

class UserInfoEditPage extends StatefulWidget {
  @override
  _UserInfoEditPageState createState() => _UserInfoEditPageState();
}

class _UserInfoEditPageState extends State<UserInfoEditPage> {
  var _userInfo; // 사용자 정보를 저장할 변수
  TextEditingController _emailController = TextEditingController();
  TextEditingController _phoneNumberController = TextEditingController();
  TextEditingController _genderController = TextEditingController();
  TextEditingController _passwordController = TextEditingController();
  TextEditingController _nameController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadUserInfo();
  }

  _loadUserInfo() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? userid = prefs.getString('userid');
    if (userid != null) {
      var url = Uri.parse('http://localhost:8080/api/user/details/$userid');
      var response = await http.get(url);
      if (response.statusCode == 200) {
        var userInfo = json.decode(response.body);
        setState(() {
          _userInfo = userInfo;
          _emailController.text = userInfo['email'] ?? '';
          _phoneNumberController.text = userInfo['phone_number'] ?? '';
          _genderController.text = userInfo['gender'] ?? '';
          _passwordController.text = userInfo['userpw'] ?? '';
          _nameController.text = userInfo['name'] ?? '';
        });
      } else {
        print('서버 오류: ${response.statusCode}');
        // 오류 처리
      }
    }
  }

  Future<void> _updateUserInfo() async {
    final prefs = await SharedPreferences.getInstance();
    final userid = prefs.getString('userid');
    if (userid != null) {
      final url = Uri.parse('http://localhost:8080/api/user/update');

      final response = await http.put(
        url,
        headers: {"Content-Type": "application/json"},
        body: jsonEncode({
          // Dart의 jsonEncode 함수 사용
          'userid': userid,
          'email': _emailController.text,
          'phone_number': _phoneNumberController.text,
          'gender': _genderController.text,
          'userpw': _passwordController.text,
          'name': _nameController.text,
        }),
      );

      if (response.statusCode == 200) {
        // 성공적으로 업데이트됨
        print('User updated successfully');
        Navigator.of(context).pushAndRemoveUntil(
          MaterialPageRoute(builder: (context) => MyPage()),
          (Route<dynamic> route) => false,
        );
      } else {
        // 업데이트 실패
        print('Failed to update user: ${response.body}');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('회원 정보 수정'),
      ),
      body: _userInfo == null
          ? Center(child: CircularProgressIndicator()) // 로딩 인디케이터
          : Padding(
              padding: EdgeInsets.all(16.0),
              child: ListView(
                children: <Widget>[
                  TextField(
                    controller: _nameController,
                    decoration: InputDecoration(labelText: '이름'),
                  ),
                  TextField(
                    controller: _emailController,
                    decoration: InputDecoration(labelText: '이메일'),
                  ),
                  TextField(
                    controller: _phoneNumberController,
                    decoration: InputDecoration(labelText: '전화번호'),
                  ),
                  TextField(
                    controller: _genderController,
                    decoration: InputDecoration(labelText: '성별'),
                  ),
                  SizedBox(height: 20),
                  ElevatedButton(
                    onPressed: () {
                      _updateUserInfo();
                    },
                    child: Text('저장'),
                  ),
                ],
              ),
            ),
    );
  }
}
