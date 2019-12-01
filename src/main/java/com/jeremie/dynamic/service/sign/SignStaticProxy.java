package com.jeremie.dynamic.service.sign;

public class SignStaticProxy implements SignInterface {

    private SignInterface signInterface;

    public SignStaticProxy(SignInterface signInterface) {
        this.signInterface = signInterface;
    }

    /**
     * 静态代理签证
     * @param myInfo 我的个人信息
     * @return 签证结果
     */
    @Override
    public String sign(String myInfo) {
        System.out.println("签证前准备");
        String result = signInterface.sign(myInfo);
        System.out.println("签证后处理");
        return result;
    }
}
