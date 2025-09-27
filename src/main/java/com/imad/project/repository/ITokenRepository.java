package com.imad.project.repository;

import com.imad.project.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITokenRepository extends MongoRepository<Token, UUID> {

    @Query("{ 'userId': ?0, '$or': [ { 'expired': false }, { 'revoked': false } ] }")
    List<Token> findAllValidTokenByUser(UUID userId);

    Optional<Token> findByToken(String token);
}
