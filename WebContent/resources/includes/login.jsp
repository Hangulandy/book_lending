<h1>Login</h1>
<p>Enter your login information:</p>
<p>
    <i class="errorMsg">${message}</i>
</p>

<form action="BookAppServlet" method="post">
    <input type="hidden" name="action" value="login" />

    <label class="pad_top">Email:</label>
    <input type="email" name="email" value="${member.email}" required />
    <br>
    <label class="pad_top">Password:</label>
    <input type="password" name="password" required />
    <br>
    <input class="button" type="submit" value="Login" class="margin_left" />
</form>