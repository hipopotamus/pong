package com.hipo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hipo.dataobjcet.dto.AccountDto;
import com.hipo.dataobjcet.dto.BasicErrorResult;
import com.hipo.dataobjcet.dto.Result;
import com.hipo.domain.UserAccount;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.Relation;
import com.hipo.domain.entity.enums.Gender;
import com.hipo.domain.entity.enums.RelationState;
import com.hipo.domain.entity.enums.Role;
import com.hipo.domain.processor.JwtProcessor;
import com.hipo.exception.DuplicationRequestException;
import com.hipo.exception.IllegalRequestException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.RelationRepository;
import com.hipo.test.PageModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RelationControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired AccountRepository accountRepository;
    @Autowired RelationRepository relationRepository;
    @Autowired JwtProcessor jwtProcessor;

    @BeforeEach
    public void init() {
        for (int i = 1; i <= 4; i++) {
            Account account = new Account("test" + i + "@test.com", bCryptPasswordEncoder.encode("1234"),
                    "testNickname" + i, "/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg",
                    Role.User, Gender.MAN, LocalDate.now());
            accountRepository.save(account);
        }
    }

    @Test
    @DisplayName("친구 요청 성공")
    public void requestFriendTest() throws Exception {

        //given
        Account fromAccount = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));

        //when
        //** test1 -> test2 친구 요청
        mockMvc.perform(post("/relation/request/" + toAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk());

        boolean request = relationRepository
                .existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.REQUEST);
        boolean toRequest = relationRepository
                .existsByFromAccountAndToAccountAndRelationStateEquals(toAccount, fromAccount, RelationState.REQUEST);

        //then
        assertThat(request).isTrue();
        assertThat(toRequest).isFalse();
    }

    @Test
    @DisplayName("친구 요청 성공_상대방만 친구 상태")
    public void requestFriend_existOpponentFriend_Test() throws Exception {

        //given
        Account fromAccount = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        relationRepository.save(new Relation(toAccount, fromAccount, RelationState.FRIEND));

        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));

        //when
        //** test1 -> test2 친구 요청
        mockMvc.perform(post("/relation/request/" + toAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken1))
                .andExpect(status().isOk());

        boolean request = relationRepository
                .existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.REQUEST);
        boolean toRequest = relationRepository
                .existsByFromAccountAndToAccountAndRelationStateEquals(toAccount, fromAccount, RelationState.REQUEST);

        //then
        assertThat(request).isTrue();
        assertThat(toRequest).isFalse();
    }

    @Test
    @DisplayName("친구 요청 실패_상대편에서 친구 요청 한 상태")
    public void requestFriend_existOpponentRequest_Test() throws Exception {

        //given
        Account fromAccount = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
        String jwtToken2 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));

        //** test1 -> test2 친구 요청
        mockMvc.perform(post("/relation/request/" + toAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken1))
                .andExpect(status().isOk());

        //when
        //** test2 -> test1 친구 요청 (상대편쪽에서 이미 친구 요청 한 상태)
        MvcResult existOpponentRequestResult = mockMvc.perform(post("/relation/request/" + fromAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken2))
                .andExpect(status().is4xxClientError())
                .andReturn();

        //then
        BasicErrorResult existOpponentRequestError = objectMapper
                .readValue(existOpponentRequestResult.getResponse().getContentAsString(), BasicErrorResult.class);

        //then
        assertThat(existOpponentRequestError.getState()).isEqualTo("400");
        assertThat(existOpponentRequestError.getException()).isEqualTo(DuplicationRequestException.class.getSimpleName());
    }

    @Test
    @DisplayName("친구 요청 실패_친구 요청 중복")
    public void requestFriend_duplicationRequest_Test() throws Exception {

        //given
        Account fromAccount = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));

        //** test1 -> test2 친구 요청
        mockMvc.perform(post("/relation/request/" + toAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken1))
                .andExpect(status().isOk());

        //when
        //** test1 -> test2 친구 요청 (친구 요청 중복)
        MvcResult duplicationRequestResult = mockMvc.perform(post("/relation/request/" + toAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken1))
                .andExpect(status().is4xxClientError())
                .andReturn();

        //then
        BasicErrorResult duplicationRequestError = objectMapper
                .readValue(duplicationRequestResult.getResponse().getContentAsString(), BasicErrorResult.class);

        //then
        assertThat(duplicationRequestError.getState()).isEqualTo("400");
        assertThat(duplicationRequestError.getException()).isEqualTo(DuplicationRequestException.class.getSimpleName());
    }

    @Test
    @DisplayName("친구 요청 실패_자기 자신에게 친구 요청")
    public void requestFriend_selfRequest_Test() throws Exception {

        //given
        Account fromAccount = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));

        //when
        //** test1 -> test2 친구 요청 (친구 요청 중복)
        MvcResult selfRequestResult = mockMvc.perform(post("/relation/request/" + fromAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken1))
                .andExpect(status().is4xxClientError())
                .andReturn();

        //then
        BasicErrorResult selfRequestError = objectMapper
                .readValue(selfRequestResult.getResponse().getContentAsString(), BasicErrorResult.class);

        //then
        assertThat(selfRequestError.getState()).isEqualTo("400");
        assertThat(selfRequestError.getException()).isEqualTo(IllegalRequestException.class.getSimpleName());
    }

    @Test
    @DisplayName("친구 요청 실패_이미 친구 상태")
    public void requestFriend_existFriend_Test() throws Exception {

        //given
        Account fromAccount = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        relationRepository.save(new Relation(fromAccount, toAccount, RelationState.FRIEND));

        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));

        //when
        //** test1 -> test2 친구 요청 (이미 친구 상태)
        MvcResult existFriendResult = mockMvc.perform(post("/relation/request/" + fromAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken1))
                .andExpect(status().is4xxClientError())
                .andReturn();

        //then
        BasicErrorResult existFriendError = objectMapper
                .readValue(existFriendResult.getResponse().getContentAsString(), BasicErrorResult.class);

        //then
        assertThat(existFriendError.getState()).isEqualTo("400");
        assertThat(existFriendError.getException()).isEqualTo(IllegalRequestException.class.getSimpleName());
    }

    @Test
    @DisplayName("친구 요청 수락 성공")
    public void acceptFriendTest() throws Exception {

        //given
        Account fromAccount = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        String jwtToken2 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));

        relationRepository.save(new Relation(fromAccount, toAccount, RelationState.REQUEST));

        //when
        mockMvc.perform(post("/relation/request/accept/" + fromAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken2))
                .andExpect(status().isOk());

        boolean fromFriend = relationRepository
                .existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.FRIEND);
        boolean toFriend = relationRepository
                .existsByFromAccountAndToAccountAndRelationStateEquals(toAccount, fromAccount, RelationState.FRIEND);

        //then
        assertThat(fromFriend).isTrue();
        assertThat(toFriend).isTrue();
    }

    @Test
    @DisplayName("친구 요청 수락 성공_상대방만 친구 상태")
    public void acceptFriend_existOpponentFriend_Test() throws Exception {

        //given
        Account fromAccount = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        String jwtToken2 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));

        relationRepository.save(new Relation(fromAccount, toAccount, RelationState.REQUEST));

        //when
        //** test1 -> test2 친구 요청 (test2만 친구 상태)
        mockMvc.perform(post("/relation/request/accept/" + fromAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken2))
                .andExpect(status().isOk());

        boolean fromFriend = relationRepository
                .existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.FRIEND);
        boolean toFriend = relationRepository
                .existsByFromAccountAndToAccountAndRelationStateEquals(toAccount, fromAccount, RelationState.FRIEND);

        //then
        assertThat(fromFriend).isTrue();
        assertThat(toFriend).isTrue();
    }

    @Test
    @DisplayName("친구 요청 수락 실패_친구 요청 없음")
    public void acceptFriend_illegalRequest_Test() throws Exception {

        //given
        Account fromAccount = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));


        //when
        //** test2 -> test1 친구 요청 수락(친구 요청 없음)
        MvcResult nonExistResourceResult = mockMvc.perform(post("/relation/request/accept/" + fromAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken))
                .andExpect(status().is4xxClientError())
                .andReturn();

        BasicErrorResult nonExistResourceError = objectMapper
                .readValue(nonExistResourceResult.getResponse().getContentAsString(), BasicErrorResult.class);

        //then
        assertThat(nonExistResourceError.getState()).isEqualTo("400");
        assertThat(nonExistResourceError.getException()).isEqualTo(NonExistResourceException.class.getSimpleName());
    }

    @Test
    @DisplayName("친구 요청중 목록 조회_List")
    public void findAllRequestTest() throws Exception {
        //given
        Account fromAccount = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount1 = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount2 = accountRepository.findByUsername("test3@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));

        //** test1 -> test2 친구 요청
        relationRepository.save(new Relation(fromAccount, toAccount1, RelationState.REQUEST));

        //** test1 -> test3 친구 요청
        relationRepository.save(new Relation(fromAccount, toAccount2, RelationState.REQUEST));

        //when
        //** test1의 친구 요청 목록 list
        MvcResult allListResult = mockMvc.perform(get("/relation/requests")
                        .param("all", "true")
                        .header("Authorization", jwtToken1))
                .andExpect(status().isOk())
                .andReturn();

        Result<List<AccountDto>> allResult = objectMapper.readValue(allListResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        List<AccountDto> requestingList = allResult.getData();


        //then
        //** test1의 친구 요청 목록 size 확인
        assertThat(requestingList.size()).isEqualTo(2);

        //** test1의 친구 요청 목록 account 확인
        int i = 2;
        for (AccountDto accountDto : requestingList) {
            assertThat(accountDto.getUsername()).isEqualTo("test" + i + "@test.com");
            i++;
        }
    }

    @Test
    @DisplayName("친구 요청중 목록 조회_Page")
    public void findRequestsTest() throws Exception {

        //given
        Account fromAccount = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount1 = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount2 = accountRepository.findByUsername("test3@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));

        //** test1 -> test2 친구 요청
        relationRepository.save(new Relation(fromAccount, toAccount1, RelationState.REQUEST));

        //** test1 -> test3 친구 요청
        relationRepository.save(new Relation(fromAccount, toAccount2, RelationState.REQUEST));

        //when
        ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.registerModule(new PageModule());
        objectMapper.registerModule(new JavaTimeModule());

        //** test1의 친구 요청 목록 page
        MvcResult pageResult = mockMvc.perform(get("/relation/requests")
                        .param("page", "1")
                        .param("size", "1")
                        .header("Authorization", jwtToken1))
                .andExpect(status().isOk())
                .andReturn();

        Page<AccountDto> accountDtoPage = objectMapper.readValue(pageResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        List<AccountDto> content = accountDtoPage.getContent();

        //then
        //** test1의 친구 요청 목록 page 확인
        assertThat(accountDtoPage.getTotalElements()).isEqualTo(2);
        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).getUsername()).isEqualTo("test3@test.com");
    }

    @Test
    @DisplayName("받은 친구 요청 목록 조회_List")
    public void findAllWaitingRequest() throws Exception {

        //given
        Account fromAccount1 = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account fromAccount2 = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findByUsername("test3@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));

        //** test1 -> test3 친구 요청
        relationRepository.save(new Relation(fromAccount1, toAccount, RelationState.REQUEST));

        //** test2 -> test3 친구 요청
        relationRepository.save(new Relation(fromAccount2, toAccount, RelationState.REQUEST));

        //when
        //** test3의 전체 친구 요청중 목록 조회
        MvcResult allListResult = mockMvc.perform(get("/relation/waitRequests")
                        .header("Authorization", jwtToken)
                        .param("all", "true"))
                .andExpect(status().isOk())
                .andReturn();

        Result<List<AccountDto>> allResult = objectMapper.readValue(allListResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        List<AccountDto> waitingRequestList = allResult.getData();

        //then
        assertThat(waitingRequestList.size()).isEqualTo(2);
        assertThat(waitingRequestList.get(0).getUsername()).isEqualTo("test1@test.com");
        assertThat(waitingRequestList.get(1).getUsername()).isEqualTo("test2@test.com");
    }

    @Test
    @DisplayName("받은 친구 요청 목록 조회_Page")
    public void findWaitingRequests() throws Exception {

        //given
        Account fromAccount1 = accountRepository.findByUsername("test1@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account fromAccount2 = accountRepository.findByUsername("test2@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findByUsername("test3@test.com")
                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));

        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));

        //** test1 -> test3 친구 요청
        relationRepository.save(new Relation(fromAccount1, toAccount, RelationState.REQUEST));

        //** test2 -> test3 친구 요청
        relationRepository.save(new Relation(fromAccount2, toAccount, RelationState.REQUEST));

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new PageModule());
        objectMapper.registerModule(new JavaTimeModule());

        //** test3의 친구 요청중 목록 page 조회
        MvcResult pageResult = mockMvc.perform(get("/relation/waitRequests")
                        .header("Authorization", jwtToken)
                        .param("page", "1")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andReturn();

        Page<AccountDto> accountDtoPage = objectMapper.readValue(pageResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        List<AccountDto> content = accountDtoPage.getContent();

        //then
        assertThat(accountDtoPage.getTotalElements()).isEqualTo(2);
        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).getUsername()).isEqualTo("test2@test.com");
    }
}