package fi.develon.vsm.config;

import fi.develon.vsm.adapter.out.db.repository.CompanyRepositoryImpl;
import fi.develon.vsm.adapter.out.db.repository.CompanyRepositoryJpaInterface;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.usecase.*;
import fi.develon.vsm.usecase.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Autowired
    private CompanyRepositoryJpaInterface repositoryJpaInterface;


    @Bean
    public CompanyRepository companyRepository() {
        return new CompanyRepositoryImpl(repositoryJpaInterface);
    }

    @Bean
    public RegisterCompanyUseCase registerCompanyUseCase(CompanyRepository companyRepository) {
        return new RegisterCompanyUseCaseImpl(companyRepository);
    }

    @Bean
    public RenameCompanyUseCase renameCompanyUseCase(CompanyRepository companyRepository) {
        return new RenameCompanyUseCaseImpl(companyRepository);
    }

    @Bean
    public ChangeParentOfCompanyUseCase changeParentOfCompanyUseCase(CompanyRepository companyRepository) {
        return new ChangeParentOfCompanyUseCaseImpl(companyRepository);
    }

    @Bean
    public GetCompanySubsidiariesUseCase getCompanySubsidiariesUseCase(CompanyRepository companyRepository) {
        return new GetCompanySubsidiariesUseCaseImpl(companyRepository);
    }

    @Bean
    public UnregisterCompanyUseCase unregisterCompanyUseCase(CompanyRepository companyRepository) {
        return new UnregisterCompanyUseCaseImpl(companyRepository);
    }
}
