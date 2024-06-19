

````
java -cp lib/jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI password=LeapSecurityKey algorithm=PBEWITHHMACSHA512ANDAES_256 input=Sx5VJzfHyxKDHoZW1sVp ivGeneratorClassName=org.jasypt.iv.RandomIvGenerator

java -cp lib/jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI password=vwB2!KtdyvKj&v4u algorithm=PBEWITHHMACSHA512ANDAES_256 input=ciqdashboard ivGeneratorClassName=org.jasypt.iv.RandomIvGenerator
````



- References

    - https://github.com/techragesh/springboot-jasypt-encrypt-demo
    - https://medium.com/@surabhijaiswal/using-jasypt-with-spring-boot-9cfabfa747e4
    