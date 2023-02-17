--
-- PostgreSQL database dump
--

-- Dumped from database version 15.1
-- Dumped by pg_dump version 15.0

-- Started on 2023-01-14 15:22:30

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2 (class 3079 OID 16384)
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- TOC entry 3324 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 16398)
-- Name: information; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.information (
    firstname character varying(20) NOT NULL,
    lastname character varying(20) NOT NULL
);


ALTER TABLE public.information OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16401)
-- Name: resultmatrix; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.resultmatrix (
    result_matrix integer NOT NULL
);


ALTER TABLE public.resultmatrix OWNER TO postgres;

-- Completed on 2023-01-14 15:22:30

--
-- PostgreSQL database dump complete
--

CREATE TABLE public.matrixergebnisse (
    zeile_i integer NOT NULL,
    spalte_j integer NOT NULL,
    ergebniss integer NOT NULL
);


ALTER TABLE public.matrixergebnisse OWNER TO postgres;