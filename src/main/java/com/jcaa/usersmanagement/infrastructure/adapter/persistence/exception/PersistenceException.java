package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

public final class PersistenceException extends RuntimeException {

  private static final String MESSAGE_SAVE = "Failed to save user with ID: '%s'.";
  private static final String MESSAGE_UPDATE = "Failed to update user with ID: '%s'.";
  private static final String MESSAGE_FIND = "Failed to find user with ID: '%s'.";
  private static final String MESSAGE_EMAIL = "Failed to find user with email: '%s'.";
  private static final String MESSAGE_ALL = "Failed to retrieve all users.";
  private static final String MESSAGE_DELETE = "Failed to delete user with ID: '%s'.";

  // EMPRESA
 private static final String MESSAGE_EMPRESA_SAVE = "Failed to save empresa with ID: '%s'.";
 private static final String MESSAGE_EMPRESA_UPDATE = "Failed to update empresa with ID: '%s'.";
 private static final String MESSAGE_EMPRESA_FIND = "Failed to find empresa with ID: '%s'.";
 private static final String MESSAGE_EMPRESA_NAME = "Failed to find empresa with name: '%s'.";
 private static final String MESSAGE_EMPRESA_ALL = "Failed to retrieve all empresas.";
 private static final String MESSAGE_EMPRESA_DELETE = "Failed to delete empresa with ID: '%s'.";


  private static final String MESSAGE_CONNECTION = "Could not establish database connection.";

 private PersistenceException(final String message, final Throwable cause) {
   super(message, cause);
 }

  // ==========================
  // USER
  // ==========================

  public static PersistenceException becauseSaveFailed(final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_SAVE, userId), cause);
  }

  public static PersistenceException becauseUpdateFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_UPDATE, userId), cause);
  }

  public static PersistenceException becauseFindByIdFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_FIND, userId), cause);
  }

  public static PersistenceException becauseFindByEmailFailed(
      final String email, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_EMAIL, email), cause);
  }

  public static PersistenceException becauseFindAllFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_ALL, cause);
  }

  public static PersistenceException becauseDeleteFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_DELETE, userId), cause);
  }

  // ==========================
  // EMPRESA
  // ==========================

  public static PersistenceException becauseEmpresaSaveFailed(final String empresaId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_EMPRESA_SAVE, empresaId), cause);
  }

  public static PersistenceException becauseEmpresaUpdateFailed(final String empresaId, final Throwable cause) {
   return new PersistenceException(String.format(MESSAGE_EMPRESA_UPDATE, empresaId), cause
    );
  }

  public static PersistenceException becauseEmpresaFindByIdFailed(final String empresaId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_EMPRESA_FIND, empresaId), cause);
  }

  public static PersistenceException becauseFindByNameFailed(final String empresaName, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_EMPRESA_NAME, empresaName), cause);
  }

  public static PersistenceException becauseEmpresaFindAllFailed(final Throwable cause) {
    return new PersistenceException(MESSAGE_EMPRESA_ALL, cause);
  }

  public static PersistenceException becauseEmpresaDeleteFailed(final String empresaId, final Throwable cause) {
    return new PersistenceException(String.format(MESSAGE_EMPRESA_DELETE, empresaId), cause
    );
  }

  // ==========================
  // CONNECTION
  // ==========================

  public static PersistenceException becauseConnectionFailed(
          final Throwable cause) {return new PersistenceException(MESSAGE_CONNECTION, cause);
  }
}