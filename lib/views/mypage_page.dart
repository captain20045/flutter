import 'package:flutter/material.dart';
import 'editprofile_page.dart'; // EditProfilePage가 있는 파일 경로로 수정하세요.
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'main_page.dart'; // MainPage 위젯을 import합니다.

class MyPage extends StatelessWidget {
  Future<void> deleteUser(BuildContext context) async {
    final prefs = await SharedPreferences.getInstance();
    final userid = prefs.getString('userid');
    final response = await http.delete(
      Uri.parse('http://localhost:8080/api/user/delete/$userid'),
    );

    if (response.statusCode == 200) {
      print('회원 삭제 성공');
      // 로그인 정보 삭제
      SharedPreferences prefs = await SharedPreferences.getInstance();
      await prefs.setBool('isLoggedIn', false);

      // 성공적으로 삭제되면, 로그인 화면 또는 홈 화면으로 이동하는 로직 구현
      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(
            builder: (context) =>
                MainPage()), // LoginPage()는 로그인 페이지의 생성자입니다. 실제 사용하는 페이지로 교체하세요.
        (Route<dynamic> route) => false,
      );
    } else {
      print('회원 삭제 실패: ${response.body}');
      // 실패 처리 로직 구현, 예를 들어 사용자에게 실패 메시지를 보여줍니다.
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text('회원 삭제에 실패하였습니다.')));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("마이페이지"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text("여기는 마이페이지입니다."),
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => UserInfoEditPage()),
                );
              },
              child: Text('회원정보 수정'),
            ),
            SizedBox(height: 20), // 버튼 사이의 공간을 추가합니다.
            ElevatedButton(
              onPressed: () => deleteUser(context), // 삭제 함수를 호출합니다.
              child: Text('회원탈퇴'),
            ),
          ],
        ),
      ),
    );
  }
}
