package com.zantabri.auth_service.services;

import com.zantabri.auth_service.repositories.AccountDetailsRepository;
import com.zantabri.auth_service.repositories.ActivationCodeRepository;
import com.zantabri.auth_service.errors.ResourceNotFoundException;
import com.zantabri.auth_service.model.AccountDetails;
import com.zantabri.auth_service.model.ActivationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Transactional
public class AccountOnBoardingServiceImpl implements AccountOnBoardingService {

    @Value("${activation.baseurl}")
    private String activationBaseUrl;

    @Value("${activation.duration.hours}")
    private int activationDurationInHours;


    private final AccountDetailsRepository accountDetailsRepository;
    private final ActivationCodeRepository activationCodeRepository;
    private final NotificationService notificationService;

    @Autowired
    public AccountOnBoardingServiceImpl(AccountDetailsRepository accountDetailsRepository, ActivationCodeRepository activationCodeRepository, NotificationService notificationService) {
        this.accountDetailsRepository = accountDetailsRepository;
        this.activationCodeRepository = activationCodeRepository;
        this.notificationService = notificationService;
    }

    @Override
    public AccountDetails register(AccountDetails accountDetails) {

        AccountDetails accountDetails1 =  accountDetailsRepository.save(accountDetails);
        accountDetails.setActivated(false);

        //generate code
        String code = generateCode();

        String content = generateActivationEmailContent(accountDetails.getUsername(), code);
        notificationService.sendNotification(accountDetails.getEmail(), "Activation Link", content);

        //save code
        activationCodeRepository.save(new ActivationCode(accountDetails.getUsername(), code, LocalDateTime.now().plus(activationDurationInHours, ChronoUnit.HOURS)));

        return accountDetails1;
    }

    @Override
    public boolean activate(String username, String code) {

        Optional<ActivationCode> activationCode = activationCodeRepository.findByUsernameAndCodeAndExpiresLessThan(username, code, LocalDateTime.now());

        if (activationCode.isEmpty()) {
            return false;
        }

        Optional<AccountDetails> optionalAccountDetails = accountDetailsRepository.findById(username);

        if (optionalAccountDetails.isEmpty()) {
            throw new ResourceNotFoundException(username, AccountDetails.class);
        }

        AccountDetails accountDetails = optionalAccountDetails.get();

        accountDetails.setActivated(true);
        accountDetailsRepository.save(accountDetails);

        return true;

    }

    private String generateCode() {

        String code;
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            int i = random.nextInt(9000) + 1000;
            code = String.valueOf(i);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem when generating the random code.");
        }

        return code;

    }

    private String generateLink(String username, String code) {
        return new StringBuilder().append(activationBaseUrl).append("?").append("urn=").append(username).append("&").append("avn=").append(code).toString();
    }

    private String generateActivationEmailContent(String username, String code) {

        return new StringBuilder().append("<p>").append("Hello ").append(username).append("<br\\>")
                .append("Please click ").append("<a ").append("href=").append("'").append(generateLink(username,code)).append("'").append("\\>").append("here").append("<\\a>").append(" to activate your account.").append("<br\\>").append("<br\\>")
                .append("Enjoy.")
                .append("<\\p>").toString();

    }
}
