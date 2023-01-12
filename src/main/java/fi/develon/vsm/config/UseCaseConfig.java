package fi.develon.vsm.config;

import fi.develon.vsm.adapter.out.db.repository.CompanyRepositoryImpl;
import fi.develon.vsm.adapter.out.db.repository.CompanyRepositoryJpaInterface;
import fi.develon.vsm.adapter.out.db.repository.StationRepositoryImpl;
import fi.develon.vsm.adapter.out.db.repository.StationRepositoryJpaInterface;
import fi.develon.vsm.domain.repository.CompanyRepository;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.company.*;
import fi.develon.vsm.usecase.impl.company.*;
import fi.develon.vsm.usecase.impl.station.*;
import fi.develon.vsm.usecase.station.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Autowired
    private CompanyRepositoryJpaInterface companyRepositoryJpaInterface;


    @Bean
    public CompanyRepository companyRepository() {
        return new CompanyRepositoryImpl(companyRepositoryJpaInterface);
    }

    @Autowired
    private StationRepositoryJpaInterface stationRepositoryJpaInterface;


    @Bean
    public StationRepository stationRepository() {
        return new StationRepositoryImpl(stationRepositoryJpaInterface);
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

    @Bean
    public GetStationsWithDistanceUseCase getStationsWithDistanceUseCase(StationRepository stationRepository) {
        return new GetStationsWithDistanceUseCaseImpl(stationRepository);
    }

    @Bean
    public AddStationToCompanyUseCase addStationToCompanyUseCase(CompanyRepository companyRepository, StationRepository stationRepository) {
        return new AddStationToCompanyUseCaseImpl(companyRepository, stationRepository);
    }

    @Bean
    public RemoveStationUseCase removeStationUseCase(CompanyRepository companyRepository, StationRepository stationRepository) {
        return new RemoveStationUseCaseImpl(companyRepository, stationRepository);
    }

    @Bean
    public RelocateStationUseCase relocateStationUseCase(StationRepository stationRepository) {
        return new RelocateStationUseCaseImpl(stationRepository);
    }

    @Bean
    public RenameStationUseCase renameStationUseCase(StationRepository stationRepository) {
        return new RenameStationUseCaseImpl(stationRepository);
    }
}
