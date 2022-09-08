package com.accenture.api.protocol

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

object errors {
  sealed trait ApiError
  case class GeneralError(message: String) extends ApiError
  object GeneralError {
    given generalErrorEncoder: JsonEncoder[GeneralError] = DeriveJsonEncoder.gen[GeneralError]
    given generalErrorDecoder: JsonDecoder[GeneralError] = DeriveJsonDecoder.gen[GeneralError]
  }
}
