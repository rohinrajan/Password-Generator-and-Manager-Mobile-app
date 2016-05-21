<?php
 define('HOST','mysql.hostinger.in');
 define	('USER','u777023861_min1');
 define ('PASS', 'android123');	
 define ('DB','u777023861_test1');
 $con = mysqli_connect(HOST,USER,PASS,DB);
 if(mysqli_connect_errno())
 {
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
 }
 else{
	$username = $_POST['username'];
	$password = $_POST['password'];
	$name = $_POST['Name'];
    $email = $_POST['Email_Id'];
	
	if($username == '' || $password == '' || $name == '' || $email == ''){
		echo "Please enter all the details in order to Sign UP";
	}
	else {
		//check to see if the username is unique 
		$query = "Select UserName from usercredentials where UserName = '$username'";
		$results = mysqli_query($con, $query);
		if(mysqli_num_rows($results) > 0){
			echo "UserName is already existing!.... Please select another UserName";
		}
		else {
			//encode the password and insert into the database
			$password = base64_encode($password);
			$query = "INSERT into usercredentials (UserName,Encry_Password,Email_Address,name) values ('$username','$password','$email','$name')";	
			if(mysqli_query($con,$query))
				echo "Successful Registeration";
			else{
				//echo "Error!.. While trying to save to database.. Please try after some time";
                                echo mysqli_error($con);
                        }
		}
	}
	mysqli_close($con); 
}
?>				