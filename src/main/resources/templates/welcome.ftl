<!DOCTYPE html>

<html lang="en">

<body>
	Hello <#if currentUser??>${currentUser.username}<#else>User, please <a href="/login-form">login</a></#if> 
</body>

</html>