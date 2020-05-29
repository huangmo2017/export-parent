import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

public class App {
    // 加密
    @Test
    public void md5() {
        // 明文    算法     密文
        //  1      md5     c4ca4238a0b923820dcc509a6f75849b
        System.out.println(new Md5Hash("1").toString());
    }

    // 加密加盐
    @Test
    public void md5Salt() {
        // 用户名
        String username = "lw@export.com";
        // 密码
        String password = "1";
        // 参数1：密码, 参数2：盐；把用户名作为盐
        Md5Hash encodePassword = new Md5Hash(password, username);
        // e1087d424b213621545713b872420c7b
        System.out.println("根据用户名作为盐，加密加盐后的结果：" + encodePassword);
    }
}