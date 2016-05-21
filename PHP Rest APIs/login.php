<?php
 define('HOST','mysql.hostinger.in');
 define	('USER','u777023861_min1');
 define ('PASS', 'android123');	
 define ('DB','u777023861_test1');
 $con = mysqli_connect(HOST,USER,PASS,DB);
 if(mysqli_connect_errno()){
	 echo "Failed to connect to MySQL: " . mysqli_connect_error();
 }
 else {
	$username = $_POST['username'];
	$password = $_POST['password'];
	
	if($username == '' || $password == ''){
		echo "Please enter username and password";
	}
	else{
		$password = base64_encode($password);
		$sql_login = "select userID,Username,Encry_Password from usercredentials where Username='$username' and Encry_Password='$password'";
		$results_login = mysqli_query($con,$sql_login);
		if (mysqli_num_rows($results_login) > 0) {
			$rowvalue = mysqli_fetch_assoc($results_login);
			$userID = $rowvalue["userID"];
			$msg = "Successful";
			echo $msg . ":" . $userID;
		}
		else {
			echo "User doesn't exist";
		}
	}
	mysqli_close($con); 
 }
?>